package isel.pdm.game.play.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.model.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.reflect.Field

class MatchFirebase(private val db: FirebaseFirestore) : Match {

    private var onGoingGame: Pair<Game, String>? = null


    private fun subscribeGameStateUpdated(
        localPlayerMarker: Marker,
        gameId: String,
        flow: ProducerScope<GameEvent>
    ) =
        db.collection(ONGOING)
            .document(gameId)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> flow.close(error)
                    snapshot != null -> {
                        snapshot.toMatchStateOrNull()?.let {
                            val game = Game(
                                localPlayerMarker = localPlayerMarker,
                                forfeitedBy = it.marker,
                                challengerBoard = it.gameBoardChallenger,
                                challengedBoard = it.gameBoardChallenged,
                                challengerMoves = it.movesChallenger,
                                challengedMoves = it.movesChallenged
                            )
                            val gameEvent = when {
                                onGoingGame == null -> GameStarted(game)
                                game.forfeitedBy != null -> GameEnded(game)
                                else -> ShotTaken(game)
                            }
                            onGoingGame = Pair(game, gameId)
                            flow.trySend(gameEvent)
                        }
                    }
                }
            }


    private suspend fun publishLocalGame(player: String, gameId: String, board: GameBoard) {
        db.collection(ONGOING)
            .document(gameId)
            .set(board.toDocumentContent(player), SetOptions.merge())
            .await()
    }


    private suspend fun getFireStoreBoard(gameId: String, boardField: String): List<Cell> {
        val docRef = db.collection(ONGOING).document(gameId)

        val getDocRef = docRef.get().await()

        val str = getDocRef.get(boardField) as String
        return str.toMovesList()

    }

    private suspend fun updateMoveMatrix(at: Coordinate, gameId: String, movesField: String){
        db.collection(ONGOING)
            .document(gameId)
            .update(movesField, FieldValue.arrayUnion(at))
            .await()
    }

    private suspend fun updateOpponentGame(game: Game, gameId: String, boardField: String) {
        db.collection(ONGOING)
            .document(gameId)
            .update(game.challengedBoard.toDocumentContent(boardField))
            .await()
    }

    override fun start(
        localPlayer: PlayerInfo,
        challenge: Challenge,
        localGameBoard: GameBoard
    ): Flow<GameEvent> {
        check(onGoingGame == null)

        return callbackFlow {
            val newGame = Game(
                localPlayerMarker = getLocalPlayerMarker(localPlayer, challenge),
                challengerBoard = GameBoard(cells = localGameBoard.cells),
            )
            val gameId = challenge.challenger.id.toString()

            var gameSubscription: ListenerRegistration? = null
            try {
                if (localPlayer == challenge.challenger) {
                    publishLocalGame(CHALLENGER_BOARD_FIELD, gameId, localGameBoard)
                    gameSubscription = subscribeGameStateUpdated(
                        localPlayerMarker = newGame.localPlayerMarker,
                        gameId = gameId,
                        flow = this
                    )

                } else {
                    publishLocalGame(CHALLENGED_BOARD_FIELD, gameId, localGameBoard)
                    gameSubscription = subscribeGameStateUpdated(
                        localPlayerMarker = newGame.localPlayerMarker,
                        gameId = gameId,
                        flow = this
                    )
                }

            } catch (e: Throwable) {
                close(e)
            }

            awaitClose {
                gameSubscription?.remove()
            }
        }
    }


    override suspend fun opponentBoardShot(
        at: Coordinate,
        localPlayer: PlayerInfo,
        challenge: Challenge,
    ) {
        onGoingGame = checkNotNull(onGoingGame).also {

            if (it.first.localPlayerMarker == Marker.LOCAL) {

                val stringBoardMoves = getFireStoreBoard(challenge.challenger.id.toString(), CHALLENGED_BOARD_FIELD)
                val challengedGameBoard = GameBoard.fromMovesList(it.first.challengerBoard.turn, stringBoardMoves)

                val game = it.copy(first = it.first.shootOpponentBoard(at, challengedGameBoard))
                updateOpponentGame(game.first,  challenge.challenger.id.toString(), CHALLENGED_BOARD_FIELD)
                updateMoveMatrix(at, challenge.challenger.id.toString(), CHALLENGER_MOVES)
            } else {
                val stringBoardMoves = getFireStoreBoard(challenge.challenger.id.toString(), CHALLENGER_BOARD_FIELD)
                val challengerGameBoard = GameBoard.fromMovesList(it.first.challengerBoard.turn, stringBoardMoves)

                val game = it.copy(first = it.first.shootOpponentBoard(at, challengerGameBoard))
                updateOpponentGame(game.first, challenge.challenger.id.toString(), CHALLENGER_BOARD_FIELD)
                updateMoveMatrix(at, challenge.challenger.id.toString(), CHALLENGED_MOVES)
            }
        }
    }

    override suspend fun forfeit() {
        onGoingGame = checkNotNull(onGoingGame).also {
            db.collection(ONGOING)
                .document(it.second)
                .update(FORFEIT_FIELD, it.first.localPlayerMarker)
                .await()
        }
    }

    override suspend fun end() {
        onGoingGame = checkNotNull(onGoingGame).let {
            db.collection(ONGOING)
                .document(it.second)
                .delete()
                .await()
            null
        }
    }
}

/**
 * Names of the fields used in the document representations.
 */
const val ONGOING = "ongoing"
const val TURN_FIELD = "turn"
const val CHALLENGER_BOARD_FIELD = "challenger_board"
const val CHALLENGED_BOARD_FIELD = "challenged_board"
const val FORFEIT_FIELD = "forfeit"
const val CHALLENGER_MOVES = "challenger_moves"
const val CHALLENGED_MOVES = "challenged_moves"


/**
 * [GameBoard] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun GameBoard.toDocumentContent(boardField: String) = mapOf(
    TURN_FIELD to turn.name,
    boardField to toMovesList().joinToString(separator = "") {
        when (it) {
            Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.BattleShip)) -> "B"
            Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Carrier)) -> "CA"
            Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Cruiser)) -> "CR"
            Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Destroyer)) -> "D"
            Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Submarine)) -> "S"
            Cell(BiStateGameCellShot.HasBeenShot, null) -> "X"
            Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.BattleShip)) -> "b"
            Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Carrier)) -> "ca"
            Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Cruiser)) -> "cr"
            Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Destroyer)) -> "d"
            Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Submarine)) -> "s"
            else -> "-"
        }
    }
)

fun Coordinate.toDocumentContent(movesField: String) = mapOf(
    movesField to this
)

data class FirebaseReader(
    val gameBoardChallenger: GameBoard,
    val gameBoardChallenged: GameBoard,
    val marker: Marker?,
    val movesChallenger: List<Coordinate>,
    val movesChallenged: List<Coordinate>
)

/**
 * Extension function to convert documents stored in the Firestore DB
 * into the corresponding match state.
 */
fun DocumentSnapshot.toMatchStateOrNull(): FirebaseReader? =
    data?.let {
        var boardChallenger = ""

        if( it[CHALLENGER_BOARD_FIELD] != null) {
            boardChallenger =  it[CHALLENGER_BOARD_FIELD] as String
        }

        var boardChallenged = ""
        if( it[CHALLENGED_BOARD_FIELD] != null) {
            boardChallenged =  it[CHALLENGED_BOARD_FIELD] as String
        }

        var movesChallenged = listOf<Map<Int,Int>>()
        if( it[CHALLENGED_MOVES] != null) {
            movesChallenged =  it[CHALLENGED_MOVES] as List<Map<Int, Int>>
        }

        var movesChallenger = listOf<Map<Int,Int>>()
        if( it[CHALLENGER_MOVES] != null) {
            movesChallenger =  it[CHALLENGER_MOVES] as List<Map<Int, Int>>
        }
        val turn = Marker.valueOf(it[TURN_FIELD] as String)
        val forfeit = it[FORFEIT_FIELD] as String?

        FirebaseReader(
            gameBoardChallenger = GameBoard.fromMovesList(turn, boardChallenger.toMovesList()),
            gameBoardChallenged = GameBoard.fromMovesList(turn, boardChallenged.toMovesList()),
            marker = if (forfeit != null) Marker.valueOf(forfeit) else null,
            movesChallenger = movesChallenger.map { map -> coordinateExtractor(map)},
            movesChallenged = movesChallenged.map { map -> coordinateExtractor(map)},
        )
    }

fun coordinateExtractor(map: Map<Int,Int>): Coordinate{
    val values = map.values.toList()
    return Coordinate(line = values[0], column = values[1])
}

/**
 * Converts this string to a list of moves in the board
 */
fun String.toMovesList(): List<Cell> {
    var idx = 0
    val cellList: MutableList<Cell> = mutableListOf()
    this.forEach { char ->
        if (char == 'A' || char == 'a' || char == 'R' || char == 'r')
            return@forEach

        cellList.add(
            when (char) {
                'B' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.BattleShip))
                'C' -> if (this[++idx] == 'A')
                    Cell(
                        BiStateGameCellShot.HasNotBeenShot,
                        Ship(TypeOfShip.Carrier)
                    )
                else Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Cruiser))


                'D' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Destroyer))
                'S' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Submarine))
                'X' -> Cell(BiStateGameCellShot.HasBeenShot, null)
                'b' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.BattleShip))
                'c' -> if (this[++idx] == 'a') Cell(
                    BiStateGameCellShot.HasBeenShot,
                    Ship(TypeOfShip.Carrier)
                ) else Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Cruiser))
                'd' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Destroyer))
                's' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Submarine))
                else -> Cell(BiStateGameCellShot.HasNotBeenShot, null)
            }
        )
        idx++
    }
    return cellList
}
