data class Coordinates(val Line: Int, val Column: Int, val Value: Boolean) {
    val Line: Int
        set(value) {
            field = value < 0 || value > 9 ? field : value
        }

    val Column: Int
        set(value) {
            field = value < 0 || value > 9 ? field : value
        }

    operator fun plus(other: Coordinates) {
        return Coordinates(Line = this.Line + other.Line, Column = this.Column + other.Column, Value = other.Value)
    }

    operator fun minus(other: Coordinates) {
        return Coordinates(Line = this.Line - other.Line, Column = this.Column - other.Column, Value = other.Value)
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