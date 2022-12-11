package isel.pdm.game.play.model

import isel.pdm.game.prep.model.Cell

data class GameCell(var cell: Cell, var hasBeenHit:Boolean = false){
    fun hit(){
        hasBeenHit = true
    }
}