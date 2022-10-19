data class Ship(public val type: ShipTypes, public val start: Coordinates, public val end: Coordinates) {
    public val size = type.size
    public var hasDrowned: Boolean
    public var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == Size
        }

    override fun  equals(other: Any?): Boolean {
        return other == this ||
                (other::class == Coordinates::class &&
                        Line == (other as Coordinates).Line &&
                        Column == (other as Coordinates).Column)
    }

    override fun toString(): String {
        return type.type + " (" + type.size + " espaços) @ (" + start.toString() + ", " + end.toString() + ')'
    }
}