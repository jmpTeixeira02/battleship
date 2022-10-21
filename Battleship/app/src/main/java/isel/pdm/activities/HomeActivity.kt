package isel.pdm.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.pdm.service.FakeMatchmaking
import isel.pdm.ui.elements.buttons.RefreshButton
import isel.pdm.ui.elements.buttons.RefreshState
import isel.pdm.ui.screen.HomeScreen
import isel.pdm.ui.screen.HomeScreenViewModel
import isel.pdm.utils.viewModelInit

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeScreenViewModel by viewModels {
        viewModelInit {
            HomeScreenViewModel(FakeMatchmaking())
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val refreshState =
                if (viewModel.isRefreshing) RefreshState.Refreshing
                else RefreshState.Ready
            HomeScreen(
                aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                replayRequest = { },
                refreshState = refreshState,
                refreshPlayers = {viewModel.findPlayer()},
                players = viewModel.players
            )
        }
    }
}
