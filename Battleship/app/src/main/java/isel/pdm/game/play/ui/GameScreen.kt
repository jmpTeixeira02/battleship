package isel.pdm.game.play.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.play.model.Game
import isel.pdm.game.prep.model.*
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.GameTopBar

val PREVIEW_MY_GAME_BOARD_SIZE: Dp = 192.dp
val OPPONENT_GAME_BOARD_SIZE: Dp = 312.dp

data class GameScreenState(
    val game: Game
)


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameScreen(
    state: GameScreenState,
    players: List<PlayerMatchmaking>,
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
   // myPrepBoard: Board,
) {

    BattleshipTheme {

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { GameTopBar(players) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MyGameBoard(
                    modifier = Modifier
                        .width(PREVIEW_MY_GAME_BOARD_SIZE)
                        .height(PREVIEW_MY_GAME_BOARD_SIZE),
                    onClick = { _, _, _ -> },
                    boardCellList = boardCellHandler.boardCellList
                )

                Spacer(modifier = Modifier.height(80.dp))

                val turnTextId =
                    if (state.game.localPlayer == state.game.board.turn) "It's your turn"
                    else "Opponent's turn"
                Text(
                    text = turnTextId,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primaryVariant
                )

                OpponentGameBoard(
                    modifier = Modifier
                        .width(OPPONENT_GAME_BOARD_SIZE)
                        .height(OPPONENT_GAME_BOARD_SIZE),
                    onClick = boardCellHandler.onCellClick,
                    boardCellList = boardCellHandler.boardCellList
                )
            }
        }

    }

}


/*@Preview
@Composable
private fun GameScreenPreview() {
    GameScreen(
        state = GameScreenState(Game(Marker.LOCAL, aBoard)),
        players = listOf(
            PlayerMatchmaking("Player 1"),
            PlayerMatchmaking("Player 2")
        ),
        boardCellHandler = BoardCellHandler(),
        myPrepBoard = Board()
    )
}*/


/*private val aBoard = GameBoard(
    turn = Marker.LOCAL,
    cells = mutableListOf(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
    )
)*/