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
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.info.AboutUsActivity
import isel.pdm.preferences.ui.CreatePlayerActivity
import isel.pdm.replay.selector.ui.SelectReplayActivity
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit
import kotlinx.coroutines.launch

class LobbyActivity : ComponentActivity() {


    companion object {
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
        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                    replayListRequest = { SelectReplayActivity.navigate(origin = this) },
                    editUserRequest = { CreatePlayerActivity.navigate(context = this) }
                ),
                onPlayerSelected = { player ->
                    viewModel.sendChallenge(player)
               },
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
                                localPlayer = it.localPlayer,
                                challenge = it.challenge,
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

