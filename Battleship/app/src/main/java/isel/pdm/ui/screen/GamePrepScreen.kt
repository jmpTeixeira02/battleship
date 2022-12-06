package isel.pdm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.data.PlayerMatchmaking
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
    onCellClick: (x: Int, y: Int, boardCell: BoardCell) -> Unit = { _, _, _ ->},
    onBoatClick: (idx: Int) -> Unit = {_->},
    selectedBoatStateList: List<BoatSelector> = List(FleetSelector.values().size){ _ -> BoatSelector.isNotSelected},
    boardCellList: List<List<BoardCell>> = List(BOARD_SIDE){ _ -> List(BOARD_SIDE){_ -> BoardCell.Water} }
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
                    selectedBoat = selectedBoatStateList.indexOfFirst{ e -> e == BoatSelector.isSelected },
                    boardCellList = boardCellList
                )
                FleetSelectorView(
                    modifier = Modifier,
                    onClick = onBoatClick,
                    selectedBoatStateList = selectedBoatStateList
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