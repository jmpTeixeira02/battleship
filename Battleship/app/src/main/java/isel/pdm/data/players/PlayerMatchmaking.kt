package isel.pdm.data.players

import isel.pdm.ui.elements.buttons.InviteState

data class PlayerMatchmaking(val name: String, val inviteState: InviteState = InviteState.InviteEnabled)