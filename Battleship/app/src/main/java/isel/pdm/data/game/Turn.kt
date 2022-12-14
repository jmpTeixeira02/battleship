package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P');

    companion object {
        fun valueOf(c: Char) : TurnUser {
            return when (c) {
                'E' -> Enemy
                'P' -> Player
                else -> throw IllegalArgumentException("No")
            }
        }
    }
}

@Parcelize
data class Turn(val user: TurnUser, val coords: Coordinate) : Parcelable

class TurnManager {
    companion object {
        fun fromString(turn: String) : Turn {
            val user = TurnUser.valueOf(turn[0])
            val coords = Coordinate.fromString(turn.substring(1))
            return Turn(user, coords)
        }

        fun toString(turn: Turn): String {
            return turn.user.type.toString() + Coordinate.toString(turn.coords)
        }
    }
}