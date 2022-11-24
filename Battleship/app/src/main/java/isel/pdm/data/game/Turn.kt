package isel.pdm.data.game

import android.os.Parcel
import android.os.Parcelable

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P')
}

data class Turn(val user: TurnUser, val coords: Coordinates) : Parcelable {

    constructor(parcel: Parcel) : this(
        TurnUser.valueOf(parcel.readString()!!),
        parcel.readParcelable(Coordinates::class.java.classLoader)!!)

    override fun toString(): String {
        return user.type.toString() + coords
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user.type.toString())
        parcel.writeParcelable(coords, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Turn> {
        override fun createFromParcel(parcel: Parcel): Turn {
            return Turn(parcel)
        }

        override fun newArray(size: Int): Array<Turn?> {
            val res = arrayOfNulls<Turn?>(size)
            for (i in 0 until size) {
                res[i] = Turn(TurnUser.Player, Coordinates.fromString("(0,0)"))
            }
            return res
        }

        fun fromString(turn: String) : Turn {
            return Turn(TurnUser.valueOf(turn[0].toString()), Coordinates.fromString(turn.substring(1)))
        }
    }
}