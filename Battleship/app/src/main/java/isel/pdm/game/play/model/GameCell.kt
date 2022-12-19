package isel.pdm.game.play.model

import isel.pdm.game.prep.model.Ship

data class GameCell(var ship: Ship?, var hasBeenHit: Boolean = false) {
    fun hit() {
        hasBeenHit = true
    }
}