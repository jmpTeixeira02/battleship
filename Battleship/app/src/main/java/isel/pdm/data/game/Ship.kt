package isel.pdm.data.game

enum class TypeOfShip(val size: Int) {
    Destroyer(2),
    Submarine(3),
    Cruiser(3),
    BattleShip( 4),
    Carrier(5)
}

data class Ship(val type: TypeOfShip) {
    val size = type.size
    var hasDrowned: Boolean = false
    var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == size
        }
}