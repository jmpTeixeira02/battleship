package isel.pdm.ui

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
import isel.pdm.data.online.players.PlayerMatchmaking
import isel.pdm.ui.elements.buttons.InviteButton
import isel.pdm.ui.elements.buttons.InviteState
import isel.pdm.ui.elements.buttons.PendingInviteButtons
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun PlayerView(
    player: PlayerMatchmaking,
    state: InviteState,
    onInviteSend: () -> Unit,
    onAcceptInvite: () -> Unit = { },
    onDeleteInvite: () -> Unit = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = player.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(all = 24.dp),
                textAlign = TextAlign.Center
            )
            if (state == InviteState.InviteEnabled || state == InviteState.InvitedDisabled) {
                InviteButton(
                    state = state,
                    onClick = { InviteState.InvitedDisabled /* quando implementado substituir por -> onInviteSend */ },
                    modifier = Modifier.padding(all = 16.dp)
                )
            } else {
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
        PlayerView(player = randomPlayer, state = InviteState.InviteEnabled, onInviteSend = { })
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInviteDisabledPreview() {
    BattleshipTheme {
        PlayerView(player = randomPlayer, state = InviteState.InvitedDisabled, onInviteSend = { })
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInvitePendingPreview() {
    BattleshipTheme {
        PlayerView(player = randomPlayer, state = InviteState.InvitePending, onInviteSend = { })
    }

}

private val randomPlayer = PlayerMatchmaking(
    name = "Jogador 1",
)
