package isel.pdm.data.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P');

    companion object {
        fun valueOf(c: Char) : TurnUser {
            return when (c) {
                'E' -> Enemy
                'P' -> Player
                else -> throw IllegalArgumentException("No turn user with corresponding character")
            }
        }
    }
}

@Parcelize
@Serializable
data class Turn(val user: TurnUser, val coords: Coordinate) : Parcelable

class TurnManager {
    companion object {
        fun fromString(turn: String) : Turn {
            val user = TurnUser.valueOf(turn[0])
            val coords = CoordinateManager.fromString(turn.substring(1))
            return Turn(user, coords)
        }

        fun equals(first: Turn, second: Turn): Boolean {
            return first == second || (first.user == second.user && first.coords == second.coords)
        }
    }
}