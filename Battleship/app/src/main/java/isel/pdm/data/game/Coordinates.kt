package isel.pdm.data.game

data class Coordinates(val Line: Int, val Column: Int, val Value: Boolean = false) {
    companion object {
        fun fromString(coords: String) : Coordinates {
            var index = coords.indexOf(", ")
            return Coordinates(Integer.parseInt(coords.subSequence(1, index).toString()), Integer.parseInt(coords.subSequence(index + 2, coords.length + 1).toString()))
        }
    }

    override fun equals(other: Any?): Boolean {
        return other == this ||
               (other!!::class == Coordinates::class &&
                Line == (other as Coordinates).Line &&
                Column == (other as Coordinates).Column)
    }

    override fun toString(): String {
        return "($Line, $Column)"
    }
}