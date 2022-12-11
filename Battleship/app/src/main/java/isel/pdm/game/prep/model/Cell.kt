package isel.pdm.game.prep.model

data class Cell(var ship: Ship?){
    var value = ship?.name ?: "Water"
}