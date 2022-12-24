package isel.pdm.game.prep.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.FakeOpponent
import isel.pdm.game.play.ui.GameActivity
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.main.MainActivity
import isel.pdm.ui.buttons.BiState
import isel.pdm.utils.viewModelInit

class GamePrepActivity : ComponentActivity() {

    companion object {
        const val LOCAL_PLAYER = "local"
        const val OPPONENT_PLAYER = "OPPONENT"
        fun navigate(origin: Activity, local: String, opponent: FakeOpponent) {
            with(origin) {
                val intent = Intent(this, GamePrepActivity::class.java)
                intent.putExtra(LOCAL_PLAYER, local)
                intent.putExtra(OPPONENT_PLAYER, opponent)
                startActivity(intent)
            }
        }
    }

    val viewModel: GamePrepViewModel by viewModels {
        viewModelInit {
            GamePrepViewModel()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val opponent: FakeOpponent = intent.getParcelableExtra(OPPONENT_PLAYER)!!
        val local: String = intent.getStringExtra(LOCAL_PLAYER)!!
        setContent {
            val deleteButtonState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            GamePrepScreen(
                players = listOf(
                    PlayerInfo(local),
                    opponent.fakeUser
                ),
                shipRemoverHandler = ShipRemoverHandler(
                    deleteButtonState = deleteButtonState,
                    onDeleteButtonClick = { viewModel.deleteBoatToggle() },
                ),
                onRandomShipPlacer = { viewModel.randomFleet() },
                boardCellHandler = BoardCellHandler(
                    onCellClick = { line: Int, column: Int, selectedShip: Ship? ->
                        viewModel.boardClickHandler(line, column, selectedShip)
                    },
                    boardCellList = viewModel.boardCells,
                ),
                shipSelectionHandler = ShipSelectionHandler(
                    selectedShip = viewModel.shipSelector
                        .filterValues { e: ShipState -> e == ShipState.isSelected }.keys.firstOrNull(),
                    shipSelector = viewModel.shipSelector,
                    onShipSelectorClick = { boatSelected: TypeOfShip ->
                        viewModel.shipSelectorHandler(boatSelected)
                    }
                ),
                onCheckBoardPrepRequest = ::checkBoardPrepState
            )
        }
    }

    private fun checkBoardPrepState() {
        if (viewModel.allShipsPlaced()) { /* time's up e barcos postos totalmente*/
            val opponent: FakeOpponent = intent.getParcelableExtra(OPPONENT_PLAYER)!!
            val local: String = intent.getStringExtra(LOCAL_PLAYER)!!
            val prepBoard = viewModel.getBoard()
            GameActivity.navigate(
                this,
                local,
                opponent.fakeUser.username,
                prepBoard,
                opponent.fakePrepBoard
            )

        } else { /* time's up e barcos não postos totalmente */
            MainActivity.navigate(this)
        }
    }
}