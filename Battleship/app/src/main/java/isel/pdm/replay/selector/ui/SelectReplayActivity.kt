package isel.pdm.replay.selector.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.model.FakeReplayService
import isel.pdm.replay.viewer.ui.ReplayGameActivity
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit

class SelectReplayActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SelectReplayActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val viewModel: SelectReplayViewModel by viewModels {
        viewModelInit {
            SelectReplayViewModel(FakeReplayService())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNewReplays()
        setContent {
            SelectReplayScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                ),
                availableReplays = viewModel.getAvailableReplays(),
                replayRequest = ReplayHandler(
                    onOpenSelectedReplay = { replay: Replay ->
                        viewModel.openReplay(replay)
                        ReplayGameActivity.navigate(origin = this, replay)
                    }
                )
            )
        }
    }
}