package isel.pdm.game.play.model

import isel.pdm.game.prep.model.Ship

data class GameShip(val ship: Ship) {
    var hasDrowned: Boolean = false
    var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == ship.size
        }
}