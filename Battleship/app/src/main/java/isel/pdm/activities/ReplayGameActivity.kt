package isel.pdm.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.data.Replay
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.screen.ReplayGameScreen

class ReplayGameActivity : ComponentActivity() {

    companion object {
        private const val REPLAY_EXTRA = "REPLAY_EXTRA"
        fun navigate(origin: Activity, replay: Replay? = null) {
            with(origin) {
                val intent = Intent(this, ReplayGameActivity::class.java)
                intent.putExtra(REPLAY_EXTRA, replay)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReplayGameScreen(
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                ),
                replay = replayExtra!!
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