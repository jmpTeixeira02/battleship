package isel.pdm.game.prep.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

enum class CellState { Water, Ship, ShotTaken }

@Parcelize
data class Cell(var state: CellState, var ship: Ship? = null) : Parcelable {

    //var value = ship?.name ?: "Water"
    @IgnoredOnParcel
    var value =
        when (state) {
            CellState.Ship -> ship?.name
            CellState.ShotTaken -> "ShotTaken"
            else -> "Water"
        }
}