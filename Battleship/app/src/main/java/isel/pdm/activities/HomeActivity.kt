package isel.pdm.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.data.players.InviteState
import isel.pdm.data.players.PlayerMatchmaking
import isel.pdm.service.FakeMatchmaking
import isel.pdm.ui.elements.buttons.RefreshState
import isel.pdm.ui.screen.HomeScreen
import isel.pdm.ui.screen.HomeScreenViewModel
import isel.pdm.utils.viewModelInit

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeScreenViewModel by viewModels {
        viewModelInit {
            HomeScreenViewModel(FakeMatchmaking())
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
                aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                replayRequest = { SelectReplayActivity.navigate(origin = this )},
                refreshState = refreshState,
                refreshPlayers = { viewModel.findPlayer() },
                players = viewModel.players,
                onInviteSent = {
                    player: PlayerMatchmaking, state: InviteState ->
                        viewModel.updatePlayerState(player, state)
                }
            )
        }
    }
}

