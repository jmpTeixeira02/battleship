package isel.pdm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import isel.pdm.R
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.service.FakeMatchmakingService
import isel.pdm.ui.elements.Fleet
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

    private fun boatSelectedClick(idx: Int){
        viewModel.updateSelectedList(idx)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val deleteBoatState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            val selectedBoatStateList =
                viewModel.isSelectedList.map{
                    bool ->
                        if (bool) BiState.hasBeenPressed
                        else BiState.hasNotBeenPressed
            }
            GamePrepScreen(
                players = listOf(
                    PlayerMatchmaking("Player 1"),
                    PlayerMatchmaking("Player 2")
                ),
                rotateBoat = {},
                deleteBoatState = deleteBoatState,
                deleteBoat = {viewModel.deleteBoat()},
                onCellClick = { x: Int, y: Int, boatColor: Color ->
                    Log.v("GamePrepActivity", "Cell: $x, $y with color $boatColor")
                    viewModel.updateCell(x, y, boatColor)
                },
                selectedBoatStateList = selectedBoatStateList,
                boardCellList = viewModel.boardCell,
                onBoatClick = {
                    idx: Int -> boatSelectedClick(idx)
                }
            )
        }
    }
}