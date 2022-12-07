package isel.pdm.data.game

data class Cell(var ship: Ship?, var hasBeenHit:Boolean = false){
    fun hit(){
        hasBeenHit = true;
    }
}