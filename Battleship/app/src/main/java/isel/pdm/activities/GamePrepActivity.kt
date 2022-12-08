package isel.pdm.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.data.game.Ship
import isel.pdm.data.game.TypeOfShip
import isel.pdm.ui.elements.ShipState
import isel.pdm.ui.elements.buttons.BiState
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
                deleteBoat = {viewModel.deleteBoatToggle()},
                onCellClick = { line: Int, column: Int, selectedShip: Ship? ->
                    viewModel.boardClickHandler(line, column, selectedShip)
                },
                selectedBoat = viewModel.shipSelector.filterValues{ e -> e == ShipState.isSelected }.keys.firstOrNull(),
                shipState = viewModel.shipSelector,
                boardCellList = viewModel.boardCells,
                onBoatSelectClick = {
                    boatSelected: TypeOfShip -> viewModel.boatSelectorHandler(boatSelected)
                }
            )
        }
    }
}