package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents coordinates
 */
@Parcelize
data class Coordinate(val line: Int, val column: Int) : Parcelable {
    init {
        require(isValidRow(line) && isValidColumn(column))
    }
    companion object{
        private val min = 0
        private val max = BOARD_SIDE
        fun random():Coordinate{
            val interval = (min until max)
            return Coordinate(interval.random(), interval.random())
        }
        fun fromString(coords: String): Coordinate {
            val index = coords.indexOf(",")
            return Coordinate(
                Integer.parseInt(coords.subSequence(1, index).toString()),
                Integer.parseInt(coords.subSequence(index + 2, coords.length + 1).toString())
            )
        }
    }
}


/**
 * Creates a coordinate with random line and column
 */


/**
 * Checks whether [value] is a valid row index
 */
fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE