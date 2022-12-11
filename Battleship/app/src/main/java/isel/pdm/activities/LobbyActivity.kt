package isel.pdm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.data.player.InviteState
import isel.pdm.data.player.PlayerMatchmaking
import isel.pdm.service.FakeMatchmakingService
import isel.pdm.ui.elements.MatchmakingHandlers
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.ui.elements.topbar.NavigationHandlers
import isel.pdm.ui.screen.HomeScreen
import isel.pdm.utils.viewModelInit

class LobbyActivity : ComponentActivity() {


    companion object {
        private const val PLAYER_EXTRA = "PLAYER_EXTRA"
        fun navigate(origin: Activity, player: PlayerMatchmaking? = null) {
            with(origin) {
                val intent = Intent(this, LobbyActivity::class.java)
                intent.putExtra(PLAYER_EXTRA, player)
                startActivity(intent)
            }
        }
    }

    private val viewModel: LobbyScreenViewModel by viewModels {
        viewModelInit {
            LobbyScreenViewModel(FakeMatchmakingService())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val refreshState =
                if (viewModel.isRefreshing) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            HomeScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                    replayListRequest = { SelectReplayActivity.navigate(origin = this) },
                    editUserRequest = { CreatePlayerActivity.navigate(context = this, ) }
                ),
                matchMakingRequest = MatchmakingHandlers(
                    onAcceptInvite = { player: PlayerMatchmaking ->
                        viewModel.removePlayer(player)
                        GamePrepActivity.navigate(origin = this)
                    },
                    onInviteSend = {
                        player: PlayerMatchmaking, state: InviteState ->
                            viewModel.updatePlayerState(player, state)
                    },
                    onDeleteInvite = { player: PlayerMatchmaking ->
                        viewModel.removePlayer(player)
                    }
                ),
                refreshState = refreshState,
                refreshPlayers = { viewModel.findPlayer() },
                players = viewModel.players,
                //currentPlayer = playerExtra
            )
        }
    }


   /* @Suppress("deprecation")
    private val playerExtra: PlayerMatchmaking?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(PLAYER_EXTRA, PlayerMatchmaking::class.java)
            else
                intent.getParcelableExtra(PLAYER_EXTRA)*/


}

