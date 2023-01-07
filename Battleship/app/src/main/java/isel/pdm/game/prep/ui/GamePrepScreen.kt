package isel.pdm.game.prep.ui

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.pdm.R
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.ui.GamePrepBoard
import isel.pdm.ui.buttons.BiState
import isel.pdm.ui.buttons.RemoveBoatButton
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.GameTopBar


val BOARD_SIZE: Dp = 248.dp


data class ShipRemoverHandler(
    val onDeleteButtonClick: () -> Unit = {},
    val deleteButtonState: BiState = BiState.hasNotBeenPressed,
)

data class BoardCellHandler(
    val onCellClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    val boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
    val onLocalPlayerShotSent: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    val localBoardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
    val opponentBoardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
)

data class ShipSelectionHandler(
    val onShipSelectorClick: (boatSelected: TypeOfShip) -> Unit = { _ -> },
    val shipSelector: Map<TypeOfShip, ShipState> = TypeOfShip.values()
        .associateWith { _ -> ShipState.isNotSelected },
    val selectedShip: TypeOfShip? = null
)

const val GamePrepScreenTestTag = "GamePrepScreen"
const val RandomButtonTestTag = "RandomButton"
const val CountdownPrepTimerTestTag = "CountdownTimer"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GamePrepScreen(
    players: List<String>,
    onRandomShipPlacer: () -> Unit = {},
    shipRemoverHandler: ShipRemoverHandler = ShipRemoverHandler(),
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
    shipSelectionHandler: ShipSelectionHandler = ShipSelectionHandler(),
    onCheckBoardPrepRequest: () -> Unit
) {
    BattleshipTheme {

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { GameTopBar(players) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(GamePrepScreenTestTag),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CountdownPrepTimer(
                    modifier = Modifier.testTag(CountdownPrepTimerTestTag),
                    gamePrepDuration = 10000,
                    onCheckBoardPrepRequest
                )
                GamePrepBoard(
                    modifier = Modifier
                        .width(BOARD_SIZE)
                        .height(BOARD_SIZE),
                    onClick = boardCellHandler.onCellClick,
                    selectedBoat = shipSelectionHandler.selectedShip,
                    boardCellList = boardCellHandler.boardCellList,
                    enabled = true
                )
                FleetSelectorView(
                    modifier = Modifier.testTag(FleetSelectorTestTag),
                    onClick = shipSelectionHandler.onShipSelectorClick,
                    shipSelector = shipSelectionHandler.shipSelector,
                )

                Button(
                    onClick = onRandomShipPlacer,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .testTag(RandomButtonTestTag)
                ) {
                    Text(text = stringResource(id = R.string.random_button))
                }

                RemoveBoatButton(
                    state = shipRemoverHandler.deleteButtonState,
                    onClick = shipRemoverHandler.onDeleteButtonClick
                )
            }
        }
    }
}

@Composable
fun CountdownPrepTimer(
    modifier: Modifier,
    gamePrepDuration: Long,
    onCheckBoardPrepRequest: () -> Unit
) {

    val minutes = gamePrepDuration / 1000 / 60
    val seconds = gamePrepDuration / 1000 % 60

    val timeData = remember { mutableStateOf(gamePrepDuration) }
    val minutesTimer = remember { mutableStateOf(minutes) }
    val secondsTimer = remember { mutableStateOf(seconds) }
    var visible by remember { mutableStateOf(false) }

    val countDownTimer =
        object : CountDownTimer(gamePrepDuration, 1000) {
            override fun onTick(timeLeft: Long) {
                timeData.value = timeLeft
                minutesTimer.value = timeData.value / 1000 / 60
                secondsTimer.value = timeData.value / 1000 % 60
            }

            override fun onFinish() {
                visible = !visible
                onCheckBoardPrepRequest()
            }

        }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (visible) {
            Text("Time's up!")
        }

        Text(
            text = if (secondsTimer.value < 10) "0${minutesTimer.value}:0${secondsTimer.value}"
            else "0${minutesTimer.value}:${secondsTimer.value}",
            color = Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    DisposableEffect(key1 = "key") {
        countDownTimer.start()
        onDispose {
            countDownTimer.cancel()
        }
    }

}


@Preview
@Composable
fun CountdownPrepTimerPreview() {
    CountdownPrepTimer(modifier = Modifier, 59000, onCheckBoardPrepRequest = {})
}

@Preview
@Composable
private fun GamePrepScreenPreview() {
    GamePrepScreen(
        players = listOf(
            "Player 1",
           "Player 2"
        ),
        onCheckBoardPrepRequest = {}
    )
}