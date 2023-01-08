package isel.pdm.game.play.model

import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.model.Coordinate
import kotlinx.coroutines.flow.Flow

/**
 * Sum type used to describe events occurring while the match is ongoing.
 *
 * [GameStarted] to signal that the game has started.
 * [ShotTaken] to signal that the shot was taken.
 * [GameEnded] to signal the game termination.
 */
sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class ShotTaken(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: Marker? = null) : GameEvent(game)

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions.
 */
interface Match {

    /**
     * Starts the match. The first to take the shot is the challenger. The game
     * is only actually in progress after its initial state is published on the flow.
     * @param [localPlayer] the local player information
     * @param [challenge] the challenge bearing the players' information
     * @return the flow of game state change events, expressed as [GameEvent] instances
     * @throws IllegalStateException if a game is in progress
     */
    fun start(localPlayer: PlayerInfo, challenge: Challenge, localGameBoard: GameBoard): Flow<GameEvent>

    /**
     * Takes a shot at the given coordinates.
     * @throws IllegalStateException if a game is not in progress or the move is illegal,
     * either because it's not the local player turn or or the cell has already been shot
     */
    suspend fun opponentBoardShot(at: Coordinate, localPlayer: PlayerInfo, challenge: Challenge)


    /**
     * Forfeits the current game.
     * @throws IllegalStateException if a game is not in progress
     */
    suspend fun forfeit()

    /**
     * Ends the match, cleaning up if necessary.
     */
    suspend fun end()
}
