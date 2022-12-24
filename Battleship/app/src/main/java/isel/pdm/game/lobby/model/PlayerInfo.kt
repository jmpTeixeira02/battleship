package isel.pdm.game.lobby.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Data type that characterizes the player information while he's in the lobby
 * @property info   The information entered by the user
 * @property id     An identifier used to distinguish players in the lobby
 */
@Parcelize
data class PlayerInfo(val username: String, val id: UUID = UUID.randomUUID()) : Parcelable {
    init {
        require(validatePlayerUsername(username))
    }
}



fun userNameOrNull(username: String): PlayerInfo? =
    if (validatePlayerUsername(username))
        PlayerInfo(username)
    else
        null

fun validatePlayerUsername(username: String) = username.isNotBlank() && username.length <= 15