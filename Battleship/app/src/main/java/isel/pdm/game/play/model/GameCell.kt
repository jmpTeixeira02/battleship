package isel.pdm.game.play.model

import isel.pdm.game.prep.model.BiStateGameCellShot
import isel.pdm.game.prep.model.Cell



class GameCell(var cell: Cell, var cellHasBeenShot: BiStateGameCellShot = BiStateGameCellShot.HasNotBeenShot) {

    var value = cell.ship?.name ?: "Water"


    fun hit() : Boolean {
        return cellHasBeenShot == BiStateGameCellShot.HasBeenShot
    }
}