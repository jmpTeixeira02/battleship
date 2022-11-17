package isel.pdm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


enum class InviteState { InviteEnabled, InvitedDisabled, InvitePending }

@Parcelize
data class PlayerMatchmaking(
    val username: String,
    var inviteState: InviteState = InviteState.InviteEnabled,
    val favoriteGames: List<Replay> = emptyList()
) : Parcelable