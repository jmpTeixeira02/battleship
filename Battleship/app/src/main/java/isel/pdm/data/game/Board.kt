package isel.pdm.data.game

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import isel.pdm.utils.CellIsAlreadyOccupiedException
import isel.pdm.utils.InvalidOrientationException

const val BOARD_SIDE: Int = 10


data class Board(var cells: MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE){ _ -> MutableList(BOARD_SIDE){ _-> Cell(null)}.toMutableStateList()}.toMutableStateList()){

    fun deleteShip(ship: Ship): Unit{
        repeat(BOARD_SIDE){ line ->
            repeat(BOARD_SIDE){column ->
                if(cells[line][column].ship == ship) cells[line][column] = Cell(null)
            }
        }
    }

    fun placeShip(start: Coordinate, end: Coordinate, ship: Ship){
        Log.v("VIEW_MO_PLACEBOAT", "$ship")
        try{
            val shipCoordinates = canPlaceShip(start, end, ship)
            shipCoordinates.forEach {
                cells[it.line][it.column] = Cell(ship)
            }
        }catch (e: Exception){
            throw e
        }
    }


    private fun canPlaceShip(start: Coordinate, end:Coordinate, ship: Ship): List<Coordinate>{
        val temp = mutableListOf<Coordinate>()
        if (start.column == end.column) { // Ship is vertical
            repeat(ship.size){ idx ->
                val curY =
                    if (end.line > start.line) start.line + idx
                    else start.line - idx
                if (cells[curY][start.column].ship != null)
                    throw CellIsAlreadyOccupiedException("There is a boat in this cell! Coordinate: $curY ${start.column}")
                temp.add(Coordinate(curY, start.column))
            }
        }
        else if (start.line == end.line){ // Ship is horizontal
            repeat(ship.size) { idx ->
                val curX =
                    if (end.column > start.column) start.column + idx
                    else start.column - idx
                if (cells[start.line][curX].ship != null)
                    throw CellIsAlreadyOccupiedException("There is a boat in this cell! Coordinate: ${start.line} $curX")
                temp.add(Coordinate(start.line, curX))
            }
        }
        else{
            throw InvalidOrientationException("Invalid Boat Orientation!")
        }
        return temp
    }

}