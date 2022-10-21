package isel.pdm.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.activities.AboutUsActivity
import isel.pdm.data.players.PlayerMatchmaking
import isel.pdm.service.FakeMatchmaking
import isel.pdm.service.Matchmaking
import isel.pdm.ui.elements.PlayerView
import isel.pdm.ui.elements.TopBar
import isel.pdm.ui.elements.buttons.InviteState
import isel.pdm.ui.elements.buttons.RefreshButton
import isel.pdm.ui.elements.buttons.RefreshState
import isel.pdm.ui.theme.BattleshipTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

/*data class PlayersListScreen(
    val players: List<PlayerView> = emptyList()
)*/


@Composable
fun HomeScreen(
    aboutUsRequest: () -> Unit,
    replayRequest: (() -> Unit)? = null,
    refreshPlayers: () -> Unit,
    refreshState: RefreshState = RefreshState.Ready,
    players: List<PlayerMatchmaking>
){
    BattleshipTheme{
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    aboutUsRequest = aboutUsRequest,
                    replayRequest = replayRequest,
                    title = stringResource(id = R.string.app_name)
                )
            }
        )
        { innerPadding ->
            Column(modifier = Modifier.fillMaxHeight() ) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1F)
                        .padding(innerPadding)

                ){
                    items(players){
                        PlayerView(player = it, state = InviteState.InviteEnabled, onInviteSend = { TODO() })
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.padding(24.dp)
                        .fillMaxWidth()
                ) {
                    RefreshButton(state = refreshState, onClick = refreshPlayers)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(){
    HomeScreen(
        aboutUsRequest = {},
        refreshPlayers = {},
        players = mutableListOf(
            PlayerMatchmaking("A"),
            PlayerMatchmaking("B"),
            PlayerMatchmaking("C"),
            PlayerMatchmaking("A"),
            PlayerMatchmaking("B"),
            PlayerMatchmaking("C"),
        )
    )
}

/*
@Preview
@Composable
private fun PlayerInvitePreview () {

}*/
