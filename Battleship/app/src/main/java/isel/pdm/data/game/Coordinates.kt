package isel.pdm.data.game

/**
 * Represents coordinates
 */
data class Coordinate(val line: Int, val column: Int) {
    init {
        require(isValidRow(line) && isValidColumn(column))
    }
}

/**
 * Checks whether [value] is a valid row index
 */
fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE