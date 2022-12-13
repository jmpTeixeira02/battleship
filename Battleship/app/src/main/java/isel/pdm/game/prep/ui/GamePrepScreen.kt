package isel.pdm.game.prep.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.ui.buttons.BiState
import isel.pdm.ui.buttons.RemoveBoatButton
import isel.pdm.ui.topbar.GameTopBar
import isel.pdm.ui.theme.BattleshipTheme

val BOARD_SIZE: Dp = 248.dp


data class ShipRemoverHandler(
    val onDeleteButtonClick: () -> Unit = {},
    val deleteButtonState: BiState = BiState.hasNotBeenPressed,
)
data class BoardCellHandler(
    val onCellClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    val boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell(null) } },
)
data class ShipSelectionHandler(
    val onShipSelectorClick: (boatSelected: TypeOfShip) -> Unit = { _ -> },
    val shipSelector: Map<TypeOfShip, ShipState> = TypeOfShip.values().associateWith { _ -> ShipState.isNotSelected },
    val selectedShip: TypeOfShip? = null
)

const val RandomButtonTestTag = "RandomButton"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GamePrepScreen(
    players: List<PlayerMatchmaking>,
    onRandomShipPlacer: () -> Unit = {},
    shipRemoverHandler: ShipRemoverHandler = ShipRemoverHandler(),
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
    shipSelectionHandler: ShipSelectionHandler = ShipSelectionHandler(),
){
    BattleshipTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { GameTopBar(players) }
        ) { _ ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                BoardView(
                    modifier = Modifier
                        .width(BOARD_SIZE)
                        .height(BOARD_SIZE),
                    onClick = boardCellHandler.onCellClick,
                    selectedBoat = shipSelectionHandler.selectedShip,
                    boardCellList = boardCellHandler.boardCellList
                )
                FleetSelectorView(
                    modifier = Modifier.testTag(FleetSelectorTestTag),
                    onClick = shipSelectionHandler.onShipSelectorClick,
                    shipSelector = shipSelectionHandler.shipSelector
                )

                Button(
                    onClick = onRandomShipPlacer,
                    modifier = Modifier.padding(all = 16.dp).testTag(RandomButtonTestTag)) {
                    Text(text = "Random")
                }

                RemoveBoatButton(state = shipRemoverHandler.deleteButtonState, onClick = shipRemoverHandler.onDeleteButtonClick)
            }
        }
    }
}

@Preview
@Composable
private fun GamePrepScreenPreview(){
    GamePrepScreen(
        players = listOf(
            PlayerMatchmaking("Player 1"),
            PlayerMatchmaking("Player 2")
        ),
    )
}