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
import isel.pdm.ui.screen.BoardCellHandler
import isel.pdm.ui.screen.GamePrepScreen
import isel.pdm.ui.screen.ShipRemoverHandler
import isel.pdm.ui.screen.ShipSelectionHandler
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
            val deleteButtonState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            GamePrepScreen(
                players = listOf(
                    PlayerMatchmaking("Player 1"),
                    PlayerMatchmaking("Player 2")
                ),
                shipRemoverHandler = ShipRemoverHandler(
                    deleteButtonState = deleteButtonState,
                    onDeleteButtonClick = {viewModel.deleteBoatToggle()},
                ),
                onRandomShipPlacer = {},
                boardCellHandler = BoardCellHandler(
                    onCellClick = { line: Int, column: Int, selectedShip: Ship? ->
                        viewModel.boardClickHandler(line, column, selectedShip)},
                    boardCellList = viewModel.boardCells,
                ),
                shipSelectionHandler = ShipSelectionHandler(
                    selectedShip = viewModel.shipSelector
                        .filterValues{ e -> e == ShipState.isSelected }.keys.firstOrNull(),
                    shipSelector = viewModel.shipSelector,
                    onShipSelectorClick = {
                        boatSelected: TypeOfShip -> viewModel.boatSelectorHandler(boatSelected)
                    }
                ),
            )
        }
    }
}