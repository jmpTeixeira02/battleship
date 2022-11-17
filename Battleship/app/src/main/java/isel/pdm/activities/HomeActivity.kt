package isel.pdm.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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


    companion object {
        private const val PLAYER_EXTRA = "PLAYER_EXTRA"
        fun navigate(origin: Activity, player: PlayerMatchmaking? = null) {
            with(origin) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(PLAYER_EXTRA, player)
                startActivity(intent)
            }
        }
    }

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
                    replayListRequest = { SelectReplayActivity.navigate(origin = this) }
                ),
                matchMakingRequest = MatchmakingHandlers(
                    onAcceptInvite = {},
                    onInviteSend = { player: PlayerMatchmaking, state: InviteState ->
                        viewModel.updatePlayerState(player, state)
                    },
                    onDeleteInvite = { player: PlayerMatchmaking ->
                        viewModel.removePlayer(player)
                    }
                ),
                refreshState = refreshState,
                refreshPlayers = { viewModel.findPlayer() },
                players = viewModel.players,
                currentPlayer = playerExtra
            )
            Log.v("PLAYER_EXTRA", playerExtra!!.username)
        }
    }


    @Suppress("deprecation")
    private val playerExtra: PlayerMatchmaking?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(PLAYER_EXTRA, PlayerMatchmaking::class.java)
            else
                intent.getParcelableExtra(PLAYER_EXTRA)


}

