package isel.pdm.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.data.players.InviteState
import isel.pdm.data.players.PlayerMatchmaking
import isel.pdm.ui.elements.buttons.InviteButton
import isel.pdm.ui.elements.buttons.PendingInviteButtons
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun PlayerView(
    player: PlayerMatchmaking,
    onInviteSend: (PlayerMatchmaking, InviteState) -> Unit = { _, _ -> },
    onAcceptInvite: () -> Unit = { },
    onDeleteInvite: () -> Unit = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = player.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(all = 24.dp),
                textAlign = TextAlign.Center
            )
            if (player.inviteState == InviteState.InviteEnabled){
                InviteButton(
                    state = player.inviteState,
                    onClick = {onInviteSend(player, InviteState.InvitedDisabled)},
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
            else if (player.inviteState == InviteState.InvitedDisabled){
                InviteButton(
                    state = player.inviteState,
                    onClick = {},
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
            else {
                PendingInviteButtons(
                    onAcceptInvite = onAcceptInvite,
                    onDeleteInvite = onDeleteInvite,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInviteEnabledPreview() {
    BattleshipTheme {
        PlayerView(player = inviteEnablePlayer)
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInviteDisabledPreview() {
    BattleshipTheme {
        PlayerView(player = inviteDisablePlayer)
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInvitePendingPreview() {
    BattleshipTheme {
        PlayerView(player = invitePendingPlayer)
    }

}

private val inviteEnablePlayer = PlayerMatchmaking(
    name = "Jogador 1",
    inviteState = InviteState.InviteEnabled
)

private val invitePendingPlayer = PlayerMatchmaking(
    name = "Jogador 2",
    inviteState = InviteState.InvitePending
)

private val inviteDisablePlayer = PlayerMatchmaking(
    name = "Jogador 3",
    inviteState = InviteState.InvitedDisabled
)
