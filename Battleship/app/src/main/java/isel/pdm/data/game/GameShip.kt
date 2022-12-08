package isel.pdm.data.game

data class GameShip(val ship: Ship) {
    var hasDrowned: Boolean = false
    var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == ship.size
        }
}