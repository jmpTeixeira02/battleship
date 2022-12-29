package isel.pdm.replay.viewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.play.ui.OPPONENT_GAME_BOARD_SIZE
import isel.pdm.game.play.ui.PREVIEW_MY_GAME_BOARD_SIZE
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.Cell
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.selector.model.GameInfo
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.topbar.NavigationTopBar
import isel.pdm.ui.theme.BattleshipTheme

const val ForwardMoveButton = "ForwardMoveButtonTag"
const val BackwardMoveButton = "BackwardMoveButtonTag"
const val MoveCouter = "MoveCouterTag"


@Composable
fun ReplayGameScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    replay: Replay,
    myReplayCells: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
    opponentReplayCells: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
    onFowardMove: () -> Unit = {},
    onBackwardMove: () -> Unit = {},
    moveNumber: Int = 0
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                NavigationTopBar(
                    navigation = navigationRequest,
                    title = "${replay.replayId} Vs. ${replay.opponentName}"
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MyGameBoard(
                    modifier = Modifier
                        .width(PREVIEW_MY_GAME_BOARD_SIZE)
                        .height(PREVIEW_MY_GAME_BOARD_SIZE),
                    onClick = {_, _, _, ->},
                    boardCellList = myReplayCells,
                    //enabled = false
                )

                Spacer(modifier = Modifier.height(80.dp))

                OpponentGameBoard(
                    modifier = Modifier
                        .width(OPPONENT_GAME_BOARD_SIZE)
                        .height(OPPONENT_GAME_BOARD_SIZE),
                    onClick = {_, _, _, ->},
                    boardCellList = opponentReplayCells,
                    //enabled = false
                )

                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    modifier = Modifier.testTag(MoveCouter),
                    text = "Move $moveNumber",
                    fontSize = 24.sp
                )
                
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier.testTag(BackwardMoveButton),
                        onClick = onBackwardMove
                    ) {
                        Text(text = "<")
                    }
                    Button(
                        modifier = Modifier.testTag(ForwardMoveButton),
                        onClick = onFowardMove
                    ) {
                        Text(text = ">")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReplayGameScreenPreview() {
    ReplayGameScreen(
        navigationRequest = NavigationHandlers(backRequest = { }),
        replay = replay
    )
}

val replay: Replay = Replay(
    replayId = "#123",
    date = "01/01/01",
    opponentName = "olabc",
    shotsFired = 17,
)