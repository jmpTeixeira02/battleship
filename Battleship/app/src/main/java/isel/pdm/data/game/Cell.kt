package isel.pdm.data.game

data class Cell(var ship: Ship?){
    var value = ship?.name ?: "Water"
}