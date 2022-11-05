package isel.pdm.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.R
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.service.FakeMatchmakingService
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.ui.elements.topbar.NavigationHandlers
import isel.pdm.ui.screen.AboutUsScreen
import isel.pdm.ui.screen.GamePrepScreen
import isel.pdm.utils.viewModelInit

class GamePrepActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, GamePrepActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val viewModel: GamePrepViewModel by viewModels {
        viewModelInit {
            GamePrepViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val deleteBoatState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            GamePrepScreen(
                players = listOf(
                    PlayerMatchmaking("Player 1"),
                    PlayerMatchmaking("Player 2")
                ),
                rotateBoat = {},
                deleteBoatState = deleteBoatState,
                deleteBoat = {viewModel.deleteBoat()}
            )
        }
    }
}