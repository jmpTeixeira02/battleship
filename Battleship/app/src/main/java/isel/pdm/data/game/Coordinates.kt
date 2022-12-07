package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates(val Line: Int, val Column: Int, val Value: Boolean = false) : Parcelable

class CoordinatesManager {
    companion object {
        fun equals(coords: Coordinates, other: Any?): Boolean {
            return other == coords ||
                    (other!!::class == Coordinates::class &&
                            coords.Line == (other as Coordinates).Line &&
                            coords.Column == other.Column)
        }

        fun toString(coords: Coordinates): String {
            return "($coords.Line,$coords.Column)"
        }

        fun hashCode(coords: Coordinates): Int {
            var result = coords.Line
            result = 31 * result + coords.Column
            result = 31 * result + coords.Value.hashCode()
            return result
        }

        fun fromString(coords: String): Coordinates {
            val index = coords.indexOf(",")
            return Coordinates(
                Integer.parseInt(coords.subSequence(1, index).toString()),
                Integer.parseInt(coords.subSequence(index + 2, coords.length + 1).toString())
            )
        }
    }
}