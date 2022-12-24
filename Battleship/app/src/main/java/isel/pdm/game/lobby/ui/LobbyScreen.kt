package isel.pdm.game.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.game.lobby.model.InviteState
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.buttons.BiState
import isel.pdm.ui.topbar.NavigationTopBar
import isel.pdm.ui.buttons.RefreshButton
import isel.pdm.ui.theme.BattleshipTheme

/*data class PlayersListScreen(
    val players: List<PlayerView> = emptyList()
)*/

const val LobbyScreenTag = "HomeScreen"

data class LobbyScreenState(
    val players: List<PlayerMatchmaking> = emptyList()
)

@Composable
fun LobbyScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    matchMakingRequest: MatchmakingHandlers = MatchmakingHandlers(),
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
                            matchMakingRequest = matchMakingRequest
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
                PlayerMatchmaking(PlayerInfo("A")),
                PlayerMatchmaking(PlayerInfo("B"), inviteState = InviteState.InvitePending),
                PlayerMatchmaking(PlayerInfo("C"), inviteState = InviteState.InvitedDisabled),
                PlayerMatchmaking(PlayerInfo("D")),
                PlayerMatchmaking(PlayerInfo("E"), inviteState = InviteState.InvitePending),
                PlayerMatchmaking(PlayerInfo("F"), inviteState = InviteState.InvitedDisabled),
            )
        ),
        matchMakingRequest = MatchmakingHandlers(onInviteSend = { _, _ -> }),
    )
}
