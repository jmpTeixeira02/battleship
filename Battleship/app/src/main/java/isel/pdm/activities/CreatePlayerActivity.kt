package isel.pdm.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.screen.CreatePlayerScreen
import isel.pdm.ui.screen.PlayerHandler
import isel.pdm.utils.viewModelInit

class CreatePlayerActivity : ComponentActivity() {

    private val viewModel: CreatePlayerViewModel by viewModels {
        viewModelInit {
            CreatePlayerViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreatePlayerScreen(
                navigationRequest = NavigationHandlers(
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                ),
                playerRequest = PlayerHandler(
                    onCreatePlayer = { player: PlayerMatchmaking ->
                        viewModel.createPlayer(player)
                        HomeActivity.navigate(origin = this, player)
                    }
                )
            )
        }
    }
}