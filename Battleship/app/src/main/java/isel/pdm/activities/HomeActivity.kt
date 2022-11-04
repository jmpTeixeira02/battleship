package isel.pdm.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.data.InviteState
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.service.FakeMatchmakingService
import isel.pdm.ui.elements.MatchmakingHandlers
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.elements.buttons.RefreshState
import isel.pdm.ui.screen.HomeScreen
import isel.pdm.utils.viewModelInit

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeScreenViewModel by viewModels {
        viewModelInit {
            HomeScreenViewModel(FakeMatchmakingService())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val refreshState =
                if (viewModel.isRefreshing) RefreshState.Refreshing
                else RefreshState.Ready
           /* val inviteState =
                if (viewModel.isInviteSent) InviteState.InvitedDisabled
                else InviteState.InviteEnabled*/
            HomeScreen(
                navigationRequest = NavigationHandlers(
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                    replayRequest = { SelectReplayActivity.navigate(origin = this )}
                ),
                matchMakingRequest = MatchmakingHandlers(
                    onAcceptInvite = { GamePrepActivity.navigate(origin = this)},
                    onInviteSend = {
                            player: PlayerMatchmaking, state: InviteState ->
                        viewModel.updatePlayerState(player, state)
                    },
                    onDeleteInvite = {player: PlayerMatchmaking ->
                        viewModel.removePlayer(player)
                    }
                ),
                refreshState = refreshState,
                refreshPlayers = { viewModel.findPlayer() },
                players = viewModel.players,


            )
        }
    }
}

