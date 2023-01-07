package isel.pdm.game.play.model

import android.os.Parcelable
import androidx.compose.runtime.toMutableStateList
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.BiStateGameCellShot
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Coordinate
import kotlinx.parcelize.Parcelize


/**
 * Represents a Battleship board. Instances are immutable.
 * @property turn   The next player to move
 * @property cells  The board tiles
 */
@Parcelize
@kotlinx.serialization.Serializable
data class GameBoard(
    var turn: Marker = Marker.firstToMove,
    var cells: MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE) { _ ->
        MutableList(
            BOARD_SIDE
        ) { _ -> Cell() }.toMutableStateList()
    }.toMutableStateList()
) : Parcelable {


    /**
     * Makes a move at the given coordinates and returns the new board instance.
     * @param at    the board's coordinate
     * @throws IllegalArgumentException if the position is already occupied
     * @return the new board instance
     */
    fun takeShot(at: Coordinate): GameBoard {
        if(this.cells[at.line][at.column].state == BiStateGameCellShot.HasBeenShot) return this
        val newGameBoardCells = this.cells
        val shotCoordinate = newGameBoardCells[at.line][at.column]

        if(newGameBoardCells[at.line][at.column].ship != null) {
            newGameBoardCells[at.line][at.column] = Cell(state = BiStateGameCellShot.HasBeenShot, ship = shotCoordinate.ship)
        } else {
            newGameBoardCells[at.line][at.column] =
                Cell(state = BiStateGameCellShot.HasBeenShot, ship = null)
        }
        return GameBoard(
            turn = turn.other,
            cells = newGameBoardCells
        )
    }


    companion object {
        fun fromMovesList(turn: Marker, moves: List<Cell>) = if(moves.isNotEmpty()){
            GameBoard(
                turn = turn,
                cells = MutableList(size = BOARD_SIDE, init = { row ->
                    MutableList(size = BOARD_SIDE, init = { col ->
                        moves[row * BOARD_SIDE + col]
                    })
                })
            )
        }
        else {
            GameBoard(
                turn = turn
            )
        }
    }


    /**
     * Converts this instance to a list of moves.
     */
    fun toMovesList(): MutableList<Cell?> = cells.flatten().toMutableList()
}

fun GameBoard.hasWon(marker: Marker) : Boolean {
    repeat(BOARD_SIDE) { line ->
        repeat(BOARD_SIDE) { column ->
            if(this.cells[line][column].state == BiStateGameCellShot.HasNotBeenShot && this.cells[line][column].ship != null) return false
        }
    }
    return true
}



open class BoardResult
class HasWinner(val winner: Marker) : BoardResult()
class OnGoing : BoardResult()


/**
 * Gets the current result of this board.
 */
fun GameBoard.getResult(): BoardResult =
    when {
        hasWon(Marker.LOCAL) -> HasWinner(Marker.LOCAL)
        hasWon(Marker.OPPONENT) -> HasWinner(Marker.OPPONENT)
        else -> OnGoing()
    }