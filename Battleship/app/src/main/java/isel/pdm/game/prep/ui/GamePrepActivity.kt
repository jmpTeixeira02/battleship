package isel.pdm.game.prep.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.ui.buttons.BiState
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
                onRandomShipPlacer = { viewModel.randomFleet() },
                boardCellHandler = BoardCellHandler(
                    onCellClick = { line: Int, column: Int, selectedShip: Ship? ->
                        viewModel.boardClickHandler(line, column, selectedShip)},
                    boardCellList = viewModel.boardCells,
                ),
                shipSelectionHandler = ShipSelectionHandler(
                    selectedShip = viewModel.shipSelector
                        .filterValues{ e: ShipState -> e == ShipState.isSelected }.keys.firstOrNull(),
                    shipSelector = viewModel.shipSelector,
                    onShipSelectorClick = {
                        boatSelected: TypeOfShip -> viewModel.shipSelectorHandler(boatSelected)
                    }
                ),
            )
        }
    }
}