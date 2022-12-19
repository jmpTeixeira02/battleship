package isel.pdm.game.play.model

import androidx.compose.runtime.toMutableStateList
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.CellState
import isel.pdm.game.prep.model.Coordinate


data class GameBoard(
    var turn: Marker = Marker.firstToMove,
    var cells:  MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE) { _ ->
        MutableList(
            BOARD_SIDE
        ) { _ -> Cell(CellState.Water) }.toMutableStateList()
    }.toMutableStateList()
) {


    fun takeShot(coordinate: Coordinate): Boolean {
        try {
            val shotCoordinates = cells[coordinate.line][coordinate.column]

            return if(shotCoordinates.state == CellState.Ship) {
                cells[coordinate.line][coordinate.column] = Cell(CellState.ShotTaken)
                turn = turn.other
                true
            } else false


        }  catch (e: Exception) {
            throw e
        }
    }
}