package isel.pdm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.data.game.BOARD_SIDE
import isel.pdm.data.game.Cell
import isel.pdm.data.game.Ship
import isel.pdm.data.game.TypeOfShip
import isel.pdm.ui.elements.*
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.ui.elements.buttons.RemoveBoatButton
import isel.pdm.ui.elements.topbar.GameTopBar
import isel.pdm.ui.theme.BattleshipTheme

val BOARD_SIZE: Dp = 248.dp

@Composable
fun GamePrepScreen(
    players: List<PlayerMatchmaking>,
    rotateBoat: () -> Unit = {},
    deleteBoat: () -> Unit = {},
    deleteBoatState: BiState = BiState.hasNotBeenPressed,
    onCellClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    onBoatSelectClick: (boatSelected: TypeOfShip) -> Unit = { _ -> },
    boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell(null) } },
    shipState: Map<TypeOfShip, ShipState> = TypeOfShip.values().associateWith { _ -> ShipState.isNotSelected },
    selectedBoat: TypeOfShip? = null
){
    BattleshipTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = {GameTopBar(players)}
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                BoardView(
                    modifier = Modifier
                        .width(BOARD_SIZE)
                        .height(BOARD_SIZE),
                    onClick = onCellClick,
                    selectedBoat = selectedBoat,
                    boardCellList = boardCellList
                )
                FleetSelectorView(
                    modifier = Modifier,
                    onClick = onBoatSelectClick,
                    shipState = shipState
                )

                Button(onClick = rotateBoat, modifier = Modifier.padding(all = 16.dp)) {
                    Text(text = "Random")
                }

                RemoveBoatButton(state = deleteBoatState, onClick = deleteBoat)
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