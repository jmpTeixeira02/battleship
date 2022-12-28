package isel.pdm.game.lobby.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import isel.pdm.DependenciesContainer
import isel.pdm.info.AboutUsActivity
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.FakeOpponentService
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.preferences.ui.CreatePlayerActivity
import isel.pdm.replay.selector.ui.SelectReplayActivity
import isel.pdm.ui.buttons.BiState
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit
import kotlinx.coroutines.launch

class LobbyActivity : ComponentActivity() {


    companion object {
        const val PLAYER_EXTRA = "PLAYER_EXTRA"
        const val LOCAL_PLAYER = "LOCAL_PLAYER"
        fun navigate(origin: Activity, player: PlayerInfo? = null) {
            with(origin) {
                val intent = Intent(this, LobbyActivity::class.java)
                Intent(this, LobbyActivity::class.java)
                intent.putExtra(LOCAL_PLAYER, player?.username)
                startActivity(intent)
            }
        }
    }

    private val viewModel: LobbyScreenViewModel by viewModels {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyScreenViewModel(lobby = app.lobby, playerRepo = app.playerRepo)
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localPlayer: String = intent.getStringExtra(LOCAL_PLAYER)!!
        val fakeOpponent = FakeOpponentService()
        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                    replayListRequest = { SelectReplayActivity.navigate(origin = this) },
                    editUserRequest = { CreatePlayerActivity.navigate(context = this) }
                ),
                onPlayerSelected = { player -> viewModel.sendChallenge(player) },
                state = LobbyScreenState(players.map { playerInfo -> playerInfo }),
            )

        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enterLobby()
                try {
                    viewModel.pendingMatch.collect {
                        if (it != null) {
                            GamePrepActivity.navigate(
                                origin = this@LobbyActivity,
                                local = it.localPlayer.username,
                                //challenge = it.challenge,
                                opponent = fakeOpponent.opponent
                            )
                        }
                    }
                }
                finally {
                    viewModel.leaveLobby()
                }
            }
        }
    }
}

