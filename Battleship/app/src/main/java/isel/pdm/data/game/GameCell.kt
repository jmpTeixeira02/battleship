package isel.pdm.data.game

data class GameCell(var cell: Cell, var hasBeenHit:Boolean = false){
    fun hit(){
        hasBeenHit = true
    }
}