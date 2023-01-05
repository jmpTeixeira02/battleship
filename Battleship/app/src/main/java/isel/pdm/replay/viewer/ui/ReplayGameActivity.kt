package isel.pdm.replay.viewer.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.game.prep.model.*
import isel.pdm.replay.selector.model.Replay
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.utils.viewModelInit

class ReplayGameActivity : ComponentActivity() {


    companion object {
        const val REPLAY_EXTRA = "REPLAY_EXTRA"
        fun navigate(origin: Activity, replay: Replay? = null) {
            with(origin) {
                val intent = Intent(this, ReplayGameActivity::class.java)
                intent.putExtra(REPLAY_EXTRA, replay)
                startActivity(intent)
            }
        }
    }

    val viewModel: ReplayGameViewModel by viewModels {
        viewModelInit {
            ReplayGameViewModel(replayExtra!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReplayGameScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                ),
                replay = replayExtra!!,
                moveNumber = viewModel.moveCounter,
                onForwardMove = { viewModel.moveForward() },
                onBackwardMove = { viewModel.moveBackward() },
                myReplayCells = viewModel.myReplayCells,
                opponentReplayCells = viewModel.opponentReplayCells
            )
        }
    }

    @Suppress("deprecation")
    private val replayExtra: Replay?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(REPLAY_EXTRA, Replay::class.java)
            else
                intent.getParcelableExtra(REPLAY_EXTRA)
}