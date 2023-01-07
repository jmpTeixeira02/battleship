package isel.pdm.replay.selector.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.model.FakeReplayService
import isel.pdm.replay.viewer.model.RealReplayService
import isel.pdm.replay.viewer.ui.ReplayGameActivity
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit

@RequiresApi(Build.VERSION_CODES.O)
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
            SelectReplayViewModel(RealReplayService(this))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SelectReplayScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                ),
                availableReplays = viewModel.replays,
                replayRequest = ReplayHandler(
                    onOpenSelectedReplay = { replay: Replay ->
                        ReplayGameActivity.navigate(origin = this, replay)
                    }
                )
            )
        }
    }
}