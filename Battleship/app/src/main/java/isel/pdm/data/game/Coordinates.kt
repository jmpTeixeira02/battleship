package isel.pdm.data.game

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates(val Line: Int, val Column: Int, val Value: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun equals(other: Any?): Boolean {
        return other == this ||
               (other!!::class == Coordinates::class &&
                Line == (other as Coordinates).Line &&
                Column == other.Column)
    }

    override fun toString(): String {
        return "($Line,$Column)"
    }

    override fun hashCode(): Int {
        var result = Line
        result = 31 * result + Column
        result = 31 * result + Value.hashCode()
        return result
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        object Parcelator : Parceler<Coordinates> {
            fun createFromParcel(parcel: Parcel): Coordinates {
                return Coordinates(parcel)
            }

            override fun newArray(size: Int): Array<Coordinates> {
                val res = Array<Coordinates>(size) {
                    Coordinates(0, 0, false)
                }
                return res
            }

            override fun create(parcel: Parcel): Coordinates {
                return Coordinates(parcel)
            }

            override fun Coordinates.write(parcel: Parcel, flags: Int) {
                parcel.writeInt(Line)
                parcel.writeInt(Column)
                parcel.writeByte(if (Value) 1 else 0)
            }
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