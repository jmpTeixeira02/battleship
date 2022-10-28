package isel.pdm.data.players


enum class InviteState { InviteEnabled, InvitedDisabled, InvitePending }

data class PlayerMatchmaking(val name: String, var inviteState: InviteState = InviteState.InviteEnabled)