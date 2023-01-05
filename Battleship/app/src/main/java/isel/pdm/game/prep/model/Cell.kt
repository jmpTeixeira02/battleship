package isel.pdm.game.prep.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


enum class BiStateGameCellShot { HasBeenShot, HasNotBeenShot }

@Parcelize
data class Cell(
    var state: BiStateGameCellShot = BiStateGameCellShot.HasNotBeenShot,
    var ship: Ship? = null
) : Parcelable {

    @IgnoredOnParcel
    var gameCellValue =
        if (state == BiStateGameCellShot.HasNotBeenShot && ship == null || state == BiStateGameCellShot.HasNotBeenShot && ship != null) "Water"
        else if (state == BiStateGameCellShot.HasBeenShot && ship == null) "ShotTaken"
        else "Ship"

    @IgnoredOnParcel
    var prepCellValue = ship?.name ?: "Water"

}