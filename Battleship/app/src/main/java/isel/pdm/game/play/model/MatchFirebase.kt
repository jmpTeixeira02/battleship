package isel.pdm.game.play.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.model.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
                                forfeitedBy = it.second,
                                /* SUS ? */localBoard = it.first
                            )
                            val gameEvent = when {
                                onGoingGame == null -> GameStarted(game)
                                game.forfeitedBy != null -> GameEnded(game, game.forfeitedBy.other)
                                else -> MoveMade(game)
                            }
                            onGoingGame = Pair(game, gameId)
                            flow.trySend(gameEvent)
                        }
                    }
                }
            }


    private suspend fun publishLocalGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .set(game.localBoard.toDocumentContent())
            .await()
    }


    private suspend fun getFireStoreBoard(gameId: String): String {
        val docRef = db.collection(ONGOING).document(gameId)
        var ret = ""

         docRef.get().addOnSuccessListener { document ->
           ret = document.getBoardAsString()!!
        }.await()

        return ret
    }

    private fun DocumentSnapshot.getBoardAsString() : String? {
        val docData = data
        if(docData != null) {
            return docData["board"] as String
        }
        return null
    }



    private suspend fun updateOpponentGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .update(game.opponentBoard.toDocumentContent())
            .await()
    }

    private suspend fun updateLocalGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .update(game.localBoard.toDocumentContent())
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
                localBoard = GameBoard(cells = localGameBoard.cells),
            )
            
            val challengerGameId = challenge.challenger.id.toString()
            val challengedGameId = challenge.challenged.id.toString()
            var gameSubscription: ListenerRegistration? = null
            try {
                if (localPlayer == challenge.challenger) {
                    publishLocalGame(newGame, challengerGameId)
                } else publishLocalGame(newGame, challengedGameId)

                gameSubscription = subscribeGameStateUpdated(
                    localPlayerMarker = newGame.localPlayerMarker,
                    gameId = challengerGameId,
                    flow = this
                )
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

                val stringBoard = getFireStoreBoard(challenge.challenged.id.toString())

                val moves = stringBoard.toMovesList()

                val challengedGameBoard = GameBoard.fromMovesList(it.first.localBoard.turn, moves)
                val game = it.copy(first = it.first.shootOpponentBoard(at, challengedGameBoard))
                updateOpponentGame(game.first,  challenge.challenged.id.toString())
                game.first.localBoard.turn = Marker.OPPONENT
                updateLocalGame(game.first, challenge.challenger.id.toString())
            } else {
                val stringBoard = getFireStoreBoard(challenge.challenger.id.toString())

                val moves = stringBoard.toMovesList()

                val challengerGameBoard = GameBoard.fromMovesList(it.first.localBoard.turn, moves)
                val game = it.copy(first = it.first.shootOpponentBoard(at, challengerGameBoard))
                updateOpponentGame(game.first, challenge.challenger.id.toString())
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
const val BOARD_FIELD = "board"
const val FORFEIT_FIELD = "forfeit"


/**
 * [GameBoard] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun GameBoard.toDocumentContent() = mapOf(
    TURN_FIELD to turn.name,
    BOARD_FIELD to toMovesList().joinToString(separator = "") {
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


/**
 * Extension function to convert documents stored in the Firestore DB
 * into the corresponding match state.
 */
fun DocumentSnapshot.toMatchStateOrNull(): Pair<GameBoard, Marker?>? =
    data?.let {
        val moves = it[BOARD_FIELD] as String
        val turn = Marker.valueOf(it[TURN_FIELD] as String)
        val forfeit = it[FORFEIT_FIELD] as String?
        Pair(
            first = GameBoard.fromMovesList(turn, moves.toMovesList()),
            second = if (forfeit != null) Marker.valueOf(forfeit) else null
        )
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
