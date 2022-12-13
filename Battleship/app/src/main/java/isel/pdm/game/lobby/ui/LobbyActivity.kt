package isel.pdm.game.lobby.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.info.AboutUsActivity
import isel.pdm.game.lobby.model.InviteState
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.lobby.model.FakeMatchmakingService
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.preferences.ui.CreatePlayerActivity
import isel.pdm.replay.selector.ui.SelectReplayActivity
import isel.pdm.ui.buttons.BiState
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit
class LobbyActivity : ComponentActivity() {


    companion object {
        const val PLAYER_EXTRA = "PLAYER_EXTRA"
        const val LOCAL_PLAYER = "LOCAL_PLAYER"
        fun navigate(origin: Activity, player: PlayerMatchmaking? = null) {
            with(origin) {
                val intent = Intent(this, LobbyActivity::class.java)
                Intent(this, LobbyActivity::class.java)
                //intent.putExtra(PLAYER_EXTRA, player)
                intent.putExtra(LOCAL_PLAYER, player?.username)
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
        val localPlayer: String = intent.getStringExtra(LOCAL_PLAYER)!!
        setContent {
            val refreshState =
                if (viewModel.isRefreshing) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            LobbyScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                    replayListRequest = { SelectReplayActivity.navigate(origin = this) },
                    editUserRequest = { CreatePlayerActivity.navigate(context = this,) }
                ),
                matchMakingRequest = MatchmakingHandlers(
                    onAcceptInvite = { player: PlayerMatchmaking ->
                        viewModel.removePlayer(player)
                        GamePrepActivity.navigate(
                            origin = this,
                            local = localPlayer,
                            opponent = player.username
                        )
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

