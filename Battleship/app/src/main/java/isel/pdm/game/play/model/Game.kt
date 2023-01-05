package isel.pdm.game.play.model

import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.lobby.model.firstToMove
import isel.pdm.game.prep.model.Coordinate


/**
 * Represents a Battleship game. Instances are immutable.
 * @property localPlayerMarker  The local player marker
 * @property forfeitedBy        The marker of the player who forfeited the game, if that was the case
 * @property challengerBoard         The local player game board
 * @property challengedBoard      The opponent player game board
 */
data class Game(
    val localPlayerMarker: Marker = Marker.firstToMove,
    val forfeitedBy: Marker? = null,
    var wonBy: Marker? = null,
    val challengerBoard: GameBoard = GameBoard(),
    val challengedBoard: GameBoard = GameBoard(turn = Marker.OPPONENT),
)

/**
 * Makes a move on this [Game], returning a new instance.
 * @param at the coordinates where the move is to be made
 * @return the new [Game] instance
 * @throws IllegalStateException if its an invalid move, either because its
 * not the local player's turn or the move cannot be made on that location
 */
fun Game.shootOpponentBoard(at: Coordinate, opponentGameBoard: GameBoard): Game {
    return copy(challengedBoard = opponentGameBoard.takeShot(at))
}

/**
 * Gets which marker is to be assigned to the local player for the given challenge.
 */
fun getLocalPlayerMarker(localPlayer: PlayerInfo, challenge: Challenge) =
    if (localPlayer == challenge.firstToMove) Marker.firstToMove
    else Marker.firstToMove.other

/**
 * Gets the game current result
 */
fun Game.getResult(): BoardResult {
    if (forfeitedBy != null) return HasWinner(forfeitedBy.other)
    else {
        if (localPlayerMarker == Marker.LOCAL) {
            if(challengerBoard.getResult() is HasWinner) return HasWinner(Marker.OPPONENT)
            else if(challengedBoard.getResult() is HasWinner) return HasWinner(localPlayerMarker)
        } else {
            if(challengedBoard.getResult() is HasWinner) return HasWinner(Marker.LOCAL)
            else if(challengerBoard.getResult() is HasWinner) return HasWinner(localPlayerMarker)
        }
    }
    return OnGoing()
}







