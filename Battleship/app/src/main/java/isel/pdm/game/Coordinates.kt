data class Coordinates(val Line: Int, val Column: Int, val Value: Boolean) {
    val Line: Int
        set(value) {
            field = value < 0 || value > 9 ? field : value
        }

    val Column: Int
        set(value) {
            field = value < 0 || value > 9 ? field : value
        }

    override fun equals(other: Any?): Boolean {
        return other == this ||
               (other::class == Coordinates::class &&
                Line == (other as Coordinates).Line &&
                Column == (other as Coordinates).Column)
    }

    override fun toString(): String {
        return "($Line, $Column)"
    }
}