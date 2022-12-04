package isel.pdm.data.game

data class Coordinates(val line: Int, val column: Int, val value: Boolean) {
    init {
        require(line in 0..9 && column in 0..9)
    }

    override fun equals(other: Any?): Boolean {
        return other == this || (
            other!!::class == Coordinates::class &&
            line == (other as Coordinates).line &&
            column == other.column
            )
    }

}