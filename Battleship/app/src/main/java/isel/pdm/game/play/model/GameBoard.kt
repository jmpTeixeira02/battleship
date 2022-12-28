package isel.pdm.game.play.model

import android.os.Parcelable
import androidx.compose.runtime.toMutableStateList
import isel.pdm.game.prep.model.*
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GameBoard(
    var turn: Marker = Marker.firstToMove,
    var cells: MutableList<MutableList<Cell>> = MutableList(BOARD_SIDE) { _ ->
        MutableList(
            BOARD_SIDE
        ) { _ -> Cell() }.toMutableStateList()
    }.toMutableStateList()
) : Parcelable {


    fun shoot(coordinate: Coordinate): Boolean {
        try {
            val shotCoordinates = cells[coordinate.line][coordinate.column]

            return if (shotCoordinates.ship != null) {
                cells[coordinate.line][coordinate.column] =
                    Cell(state = BiStateGameCellShot.HasBeenShot, ship = shotCoordinates.ship)
                true
            } else {
                cells[coordinate.line][coordinate.column] =
                    Cell(state = BiStateGameCellShot.HasBeenShot, ship = null)
                false
            }
        } catch (e: Exception) {
            throw e
        }
    }
}