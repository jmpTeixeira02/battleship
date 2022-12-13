package isel.pdm.game.prep.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        fun navigate(origin: Activity, local:String, opponent: String) {
            with(origin) {
                val intent = Intent(this, GamePrepActivity::class.java)
                intent.putExtra("local", local)
                intent.putExtra("opponent", opponent)
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
        val opponent: String = intent.getStringExtra("opponent")!!
        val local: String = intent.getStringExtra("local")!!
        setContent {
            val deleteButtonState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            GamePrepScreen(
                players = listOf(
                    PlayerMatchmaking(local),
                    PlayerMatchmaking(opponent)
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