package isel.pdm.game.lobby.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun PlayerView(
    player: PlayerInfo,
    onPlayerSelected: (PlayerInfo) -> Unit
) {
    Card(
        modifier = Modifier
            .testTag("PlayerView")
            .fillMaxWidth()
            .clickable{onPlayerSelected(player)}
            .padding(4.dp),
        elevation = 10.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = player.username,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(all = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInviteEnabledPreview() {
    BattleshipTheme {
        PlayerView(
            player = PlayerInfo("Jogador 1"),
            onPlayerSelected = { }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInviteDisabledPreview() {
    BattleshipTheme {
        PlayerView(
            player = PlayerInfo("Jogador 2"),
            onPlayerSelected = { }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerViewInvitePendingPreview() {
    BattleshipTheme {
        PlayerView(
            player = PlayerInfo("Jogador 3"),
            onPlayerSelected = { })
    }

}