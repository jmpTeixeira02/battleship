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
                                /* SUS */localBoard = it.first
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



    /* PROBLEMA AQUI ? */

    private suspend fun publishGame(game: Game, gameId: String) {
            db.collection(ONGOING)
                .document(gameId)
                .set(game.localBoard.toDocumentContent())
                .await()
    }


    private suspend fun updateLocalGame(game: Game, gameId: String) {
           db.collection(ONGOING)
               .document(gameId)
               .update(game.localBoard.toDocumentContent())
               .await()
    }


    private suspend fun updateOpponentGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .update(game.opponentBoard.toDocumentContent())
            .await()
    }

    override fun start(localPlayer: PlayerInfo, challenge: Challenge, gameBoard: GameBoard): Flow<GameEvent> {
        check(onGoingGame == null)

        return callbackFlow {
            val newGame = Game(
                localPlayerMarker = getLocalPlayerMarker(localPlayer, challenge),
                localBoard = GameBoard(cells = gameBoard.cells ),
                opponentBoard = GameBoard(cells = gameBoard.cells)
            )
            val gameId = challenge.challenger.id.toString()
            Log.v("NEW_GAME", newGame.toString()+ "\n"+ gameId)
            var gameSubscription: ListenerRegistration? = null
            try {
                if (localPlayer == challenge.challenged) {
                    publishGame(newGame, gameId)
                }

                gameSubscription = subscribeGameStateUpdated(
                    localPlayerMarker = newGame.localPlayerMarker,
                    gameId = gameId,
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

    override suspend fun takeLocalBoardShot(at: Coordinate) {
        onGoingGame = checkNotNull(onGoingGame).also {
            val game = it.copy(first = it.first.takeLocalBoardShot(at))
            updateLocalGame(game.first, game.second)
        }
    }

    override suspend fun takeOpponentBoardShot(at: Coordinate) {
        onGoingGame = checkNotNull(onGoingGame).also {
            val game = it.copy(first = it.first.takeOpponentBoardShot(at))
            updateOpponentGame(game.first, game.second)
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
    BOARD_FIELD to toMovesList().joinToString(separator = "|") {
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
fun String.toMovesList(): List<Cell> = map {
    when (it) {
        'B' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.BattleShip))
        'C' -> if (this[1] == 'A') Cell(
            BiStateGameCellShot.HasNotBeenShot,
            Ship(TypeOfShip.Carrier)
        ) else Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Cruiser))
        'D' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Destroyer))
        'S' -> Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Submarine))
        'X' -> Cell(BiStateGameCellShot.HasBeenShot, null)
        'b' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.BattleShip))
        'c' -> if (this[1] == 'a') Cell(
            BiStateGameCellShot.HasNotBeenShot,
            Ship(TypeOfShip.Carrier)
        ) else Cell(BiStateGameCellShot.HasNotBeenShot, Ship(TypeOfShip.Cruiser))
        'd' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Destroyer))
        's' -> Cell(BiStateGameCellShot.HasBeenShot, Ship(TypeOfShip.Submarine))
        else -> Cell(BiStateGameCellShot.HasNotBeenShot, null)
    }
}