package isel.pdm.game.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.topbar.NavigationTopBar

/*data class PlayersListScreen(
    val players: List<PlayerView> = emptyList()
)*/

const val LobbyScreenTag = "HomeScreen"

data class LobbyScreenState(
    val players: List<PlayerInfo> = emptyList()
)

@Composable
fun LobbyScreen(
    onPlayerSelected: (PlayerInfo) -> Unit = { },
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    state: LobbyScreenState = LobbyScreenState(),
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                NavigationTopBar(
                    navigation = navigationRequest,
                    title = stringResource(id = R.string.app_name)
                )
            }
        )
        { innerPadding ->
            Column(modifier = Modifier.fillMaxHeight()) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1F)
                        .padding(innerPadding)

                ) {
                    items(state.players) {
                        PlayerView(
                            player = it,
                            onPlayerSelected
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LobbyScreenPreview() {
    LobbyScreen(
        navigationRequest = NavigationHandlers(replayListRequest = {}, aboutUsRequest = {}),
        state = LobbyScreenState(
            mutableListOf(
                PlayerInfo("A"),
                PlayerInfo("B"),
                PlayerInfo("C"),
                PlayerInfo("D"),
                PlayerInfo("E"),
                PlayerInfo("F"),
            )
        ),
    )
}
