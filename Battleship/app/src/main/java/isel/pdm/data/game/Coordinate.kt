package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Represents coordinates
 */
@Parcelize
@Serializable
data class Coordinate(val line: Int, val column: Int) : Parcelable {
    init {
        require(isValidRow(line) && isValidColumn(column))
    }
}

class CoordinateManager {
    companion object{
        private val min = 0
        private val max = BOARD_SIDE

        fun random() : Coordinate {
            val interval = (min until max)
            return Coordinate(interval.random(), interval.random())
        }

        fun fromString(coords: String): Coordinate {
            val index = coords.indexOf(",")
            val x = coords.substring(1, index)
            val y = coords.substring(index + 1, coords.length - 1)

            return Coordinate(Integer.parseInt(x), Integer.parseInt(y))
        }

        fun equals(first: Coordinate, second: Coordinate): Boolean {
            return first == second || (first.line == second.line && first.column == second.column)
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