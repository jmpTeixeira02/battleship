package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P')
}

@Parcelize
data class Turn(val user: TurnUser, val coords: Coordinate) : Parcelable

class TurnManager {
    companion object {
        fun fromString(turn: String) : Turn {
            return Turn(TurnUser.valueOf(turn[0].toString()), Coordinate.fromString(turn.substring(1)))
        }

        fun toString(turn: Turn): String {
            return turn.user.type.toString() + turn.coords
        }
    }
}