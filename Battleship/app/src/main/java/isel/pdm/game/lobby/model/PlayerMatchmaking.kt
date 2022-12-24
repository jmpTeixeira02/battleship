package isel.pdm.game.lobby.model

import android.os.Parcelable
import isel.pdm.replay.selector.model.Replay
import kotlinx.parcelize.Parcelize


enum class InviteState { InviteEnabled, InvitedDisabled, InvitePending }

@Parcelize
data class PlayerMatchmaking(
    val playerInfo: PlayerInfo,
    var inviteState: InviteState = InviteState.InviteEnabled,
    val favoriteGames: List<Replay> = emptyList()
) : Parcelable