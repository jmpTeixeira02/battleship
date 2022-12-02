package isel.pdm.data.game

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P')
}

@Parcelize
data class Turn(val user: TurnUser, val coords: Coordinates) : Parcelable {

    constructor(parcel: Parcel) : this(
        TurnUser.valueOf(parcel.readString()!!),
        parcel.readParcelable(Coordinates::class.java.classLoader, Coordinates::class.java)!!)

    override fun toString(): String {
        return user.type.toString() + coords
    }

    companion object {
        object Parcelator : Parceler<Turn> {
            override fun Turn.write(parcel: Parcel, flags: Int) {
                parcel.writeString(user.type.toString())
                parcel.writeParcelable(coords, flags)
            }

            override fun create(parcel: Parcel): Turn {
                return Turn(parcel)
            }
        }

        fun fromString(turn: String) : Turn {
            return Turn(TurnUser.valueOf(turn[0].toString()), Coordinates.fromString(turn.substring(1)))
        }
    }

}