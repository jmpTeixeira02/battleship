package isel.pdm.game.lobby.model

import android.os.Parcelable
import isel.pdm.replay.selector.model.Replay
import kotlinx.parcelize.Parcelize


enum class InviteState { InviteEnabled, InvitedDisabled, InvitePending }

@Parcelize
data class PlayerMatchmaking(
    val username: String,
    var inviteState: InviteState = InviteState.InviteEnabled,
    val favoriteGames: List<Replay> = emptyList()
) : Parcelable {
    init {
        require(validatePlayerUsername(username))
    }
}


fun userNameOrNull(username: String): PlayerMatchmaking? =
    if (validatePlayerUsername(username))
        PlayerMatchmaking(username)
    else
        null

fun validatePlayerUsername(username: String) = username.isNotBlank() && username.length <= 15