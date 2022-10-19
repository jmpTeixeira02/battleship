enum class ShipTypes(val type: String, val size: Int) {
    Carrier("Porta-aviões", 5),
    Battleship("Couraçado", 4),
    Destroyer("Destruidor", 3),
    Submarine("Submarino", 3),
    PatrolBoat("Patrulha", 2)
}

data class Ship(val type: ShipTypes, val start: Coordinates, val end: Coordinates) {
    val size = type.size
    var hasDrowned: Boolean = false
    var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == size
        }

    override fun equals(other: Any?): Boolean {
        return other == this ||
                (other!!::class == Ship::class &&
                        start == (other as Ship).start &&
                        end == (other as Ship).end &&
                        type == (other as Ship).type)
    }

    override fun toString(): String {
        return type.type + " (" + type.size + " espaços) @ (" + start.toString() + ", " + end.toString() + ')'
    }
}