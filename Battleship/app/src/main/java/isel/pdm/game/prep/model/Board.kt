package isel.pdm.game.prep.model

import android.os.Parcelable
import androidx.compose.runtime.toMutableStateList
import isel.pdm.game.play.model.GameBoard
import isel.pdm.utils.CellIsAlreadyOccupiedException
import isel.pdm.utils.InvalidOrientationException
import kotlinx.parcelize.Parcelize

const val BOARD_SIDE: Int = 10
const val MAX_BOAT_PLACING_TRIES: Int = 100

@Parcelize
data class Board(
    var cells: MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE) { _ ->
        MutableList(
            BOARD_SIDE
        ) { _ -> Cell() }.toMutableStateList()
    }.toMutableStateList()
): Parcelable {


    fun randomFleet() {
        clearBoard()
        TypeOfShip.values().forEach { ship -> randomPlaceShip(Ship(ship)) }
    }

    fun deleteShip(ship: Ship) {
        repeat(BOARD_SIDE) { line ->
            repeat(BOARD_SIDE) { column ->
                if (cells[line][column].ship == ship) cells[line][column] = Cell()
            }
        }
    }

    fun placeShip(start: Coordinate, end: Coordinate, ship: Ship) {
        try {
            val shipCoordinates = canPlaceShip(start, end, ship)
            shipCoordinates.forEach {
                cells[it.line][it.column] = Cell(ship = ship)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun clearBoard() {
        repeat(BOARD_SIDE) { line ->
            repeat(BOARD_SIDE) { column ->
                cells[line][column] = Cell()
            }
        }
    }

    private fun randomPlaceShip(ship: Ship) {
        repeat(MAX_BOAT_PLACING_TRIES) {
            try {
                placeShip(Coordinate.random(), Coordinate.random(), ship)
                return
            } catch (e: Exception) {

            }
        }
    }

    private fun canPlaceShip(start: Coordinate, end: Coordinate, ship: Ship): List<Coordinate> {
        val temp = mutableListOf<Coordinate>()
        if (start.column == end.column) { // Ship is vertical
            repeat(ship.size) { idx ->
                val curY =
                    if (end.line > start.line) start.line + idx
                    else start.line - idx
                if (cells[curY][start.column].ship != null)
                    throw CellIsAlreadyOccupiedException("There is a boat in this cell! Coordinate: $curY ${start.column}")
                temp.add(Coordinate(curY, start.column))
            }
        } else if (start.line == end.line) { // Ship is horizontal
            repeat(ship.size) { idx ->
                val curX =
                    if (end.column > start.column) start.column + idx
                    else start.column - idx
                if (cells[start.line][curX].ship != null)
                    throw CellIsAlreadyOccupiedException("There is a boat in this cell! Coordinate: ${start.line} $curX")
                temp.add(Coordinate(start.line, curX))
            }
        } else {
            throw InvalidOrientationException("Invalid Boat Orientation!")
        }
        return temp
    }

}