package isel.pdm.game.play.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.Game
import isel.pdm.game.play.model.Marker
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.GameTopBar

val PREVIEW_MY_GAME_BOARD_SIZE: Dp = 192.dp
val OPPONENT_GAME_BOARD_SIZE: Dp = 312.dp

data class GameScreenState(
    val title: String?,
    val game: Game
)

const val GameScreenTestTag = "GameScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameScreen(
    players: List<String>,
    state: GameScreenState,
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
    winner: Boolean
) {

    BattleshipTheme {

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { GameTopBar(players) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .testTag(GameScreenTestTag),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MyGameBoard(
                    modifier = Modifier
                        .width(PREVIEW_MY_GAME_BOARD_SIZE)
                        .height(PREVIEW_MY_GAME_BOARD_SIZE),
                    onClick = {_, _, _ -> }/*boardCellHandler.onOpponentPlayerShotTaken*/,
                    boardCellList = boardCellHandler.localBoardCellList,
                )

                Spacer(modifier = Modifier.height(80.dp))

                val titleTextId = when {
                    state.title != null -> state.title
                    state.game.localPlayerMarker == state.game.localBoard.turn ->
                        "It's your turn!"
                    else -> "Opponent's turn!"
                }

                val winnerTextId =
                    if (state.game.localBoard.turn == Marker.LOCAL) "${players[0]} wins!"
                    else "${players[1]} wins!"


                Text(
                    text = if(winner) winnerTextId else titleTextId,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primaryVariant
                )

                OpponentGameBoard(
                    modifier = Modifier
                        .width(OPPONENT_GAME_BOARD_SIZE)
                        .height(OPPONENT_GAME_BOARD_SIZE),
                    onClick = boardCellHandler.onLocalPlayerShotTaken,
                    boardCellList = boardCellHandler.opponentBoardCellList,
                    enabled = state.game.localPlayerMarker == state.game.localBoard.turn // só se o turn permitir
                )
            }
        }

    }
}
