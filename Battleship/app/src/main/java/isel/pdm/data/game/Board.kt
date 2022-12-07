package isel.pdm.data.game

import android.util.Log
import kotlinx.coroutines.delay
import java.security.InvalidParameterException

const val BOARD_SIDE: Int = 10


data class Board(var cells:MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE){MutableList(BOARD_SIDE){_ -> Cell(null)}}){

    suspend fun deleteBoat(): Unit{
        delay(500)
        // TODO()
    }

    private fun canPlaceShip(start: Coordinate, end:Coordinate, ship: Ship): List<Coordinate>{
        val temp = mutableListOf<Coordinate>()
        if (start.x == end.x) { // Ship is vertical
            repeat(ship.size){ idx ->
                val curY =
                    if (end.y > start.y) start.y + idx
                    else start.y - idx
                if (cells[curY][start.x].ship != null)
                    throw IllegalStateException()
                temp.add(Coordinate(curY, start.x))
            }
        }
        else if (start.y == end.y){ // Ship is horizontal
            repeat(ship.size) { idx ->
                val curX =
                    if (end.x > start.x) start.x + idx
                    else start.x - idx
                if (cells[start.y][curX].ship != null)
                    throw IllegalStateException()
                temp.add(Coordinate(start.y, curX))
            }
        }
        else{
            throw InvalidParameterException()
        }
        return temp
    }

    fun placeBoat(start: Coordinate, end: Coordinate, ship: Ship){
        try{
            val shipCoordinates = canPlaceShip(start, end, ship)
            shipCoordinates.forEach {
                cells[it.y][it.x].ship = ship
            }
        }catch (e: Exception){
            throw e
        }
    }

}