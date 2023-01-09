package isel.pdm.game.play.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.GameTopBar
import isel.pdm.R
import isel.pdm.game.play.model.*
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.game.prep.ui.FleetSelectorView
import isel.pdm.game.prep.ui.ShipState
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.model.GameInfo

val PREVIEW_MY_GAME_BOARD_SIZE: Dp = 160.dp
val OPPONENT_GAME_BOARD_SIZE: Dp = 220.dp

const val ForfeitButtonTag = "ForfeitButton"
const val FavoritesButtonTag = "FavoritesButton"
const val GameScreenTitleTag = "GameScreenTitle"
const val GameScreenTestTag = "GameScreen"

data class GameScreenState(
    val game: Game,
    val matchState: MatchState
)


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameScreen(
    players: List<String>,
    state: GameScreenState,
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
    onForfeitRequested: () -> Unit = { },
    onAddToFavoritesRequested: (replayName: Replay) -> Unit = {},
    result: BoardResult,
    destroyedShips: Map<TypeOfShip, ShipState> = TypeOfShip.values()
        .associateWith { _ -> ShipState.isNotSelected },
) {

    BattleshipTheme {

        var displayedReplayName by remember { mutableStateOf("") }

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
                    onClick = {_, _, _ -> },
                    boardCellList = boardCellHandler.localBoardCellList,
                )

                Spacer(modifier = Modifier.height(5.dp))

                val titleTextId = when (state.matchState) {
                    MatchState.FINISHED ->
                        if (result is HasWinner &&
                            result.winner == state.game.localPlayerMarker
                        )
                            R.string.match_ended_dialog_text_won
                        else
                            R.string.match_ended_dialog_text_lose

                    MatchState.STARTED -> if (state.game.localPlayerMarker ==
                        state.game.challengerBoard.turn
                    )
                        R.string.game_screen_your_turn
                    else R.string.game_screen_opponent_turn

                    else -> R.string.game_screen_waiting

                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )

                OpponentGameBoard(
                    modifier = Modifier
                        .width(OPPONENT_GAME_BOARD_SIZE)
                        .height(OPPONENT_GAME_BOARD_SIZE),
                    onClick = boardCellHandler.onLocalPlayerShotSent,
                    boardCellList = boardCellHandler.opponentBoardCellList,
                    enabled = state.game.localPlayerMarker == state.game.challengerBoard.turn
                )
                FleetSelectorView(
                    modifier = Modifier
                        .scale(0.75F, 0.75F),
                    shipSelector = destroyedShips
                )

                if (state.matchState == MatchState.FINISHED) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            singleLine = true,
                            value = displayedReplayName,
                            onValueChange = {
                                if (it.length <= 15) displayedReplayName = it.trim()
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.game_screen_add_to_favourites),

                                    )
                            },
                            modifier = Modifier
                                .size(width = 200.dp, height = 50.dp)
                                .padding(horizontal = 4.dp)
                        )
                        Button(
                            onClick = { onAddToFavoritesRequested(
                                Replay(
                                    replayName = displayedReplayName,
                                    opponentName = players[1],
                                    shotsFired = state.game.challengerMoves.size + state.game.challengedMoves.size,
                                    gameInfo = GameInfo(
                                        myBoard = if (state.game.localPlayerMarker == Marker.LOCAL) state.game.challengerBoard else state.game.challengedBoard,
                                        opponentBoard = if (state.game.localPlayerMarker != Marker.LOCAL) state.game.challengerBoard else state.game.challengedBoard,
                                        iMadeFirstMove = state.game.localPlayerMarker == Marker.LOCAL,
                                        myMoves = if (state.game.localPlayerMarker == Marker.LOCAL) state.game.challengerMoves else state.game.challengedMoves,
                                        opponentMoves = if (state.game.localPlayerMarker != Marker.LOCAL) state.game.challengerMoves else state.game.challengedMoves
                                    ),
                                    winner = if (result is HasWinner &&
                                        result.winner == state.game.localPlayerMarker
                                    ) players[0] else players[1]
                                )
                            ) },
                            modifier = Modifier
                                .testTag(FavoritesButtonTag)
                                .size(width = 80.dp, height = 40.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.game_screen_accept_additon_to_favourites),
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onForfeitRequested,
                        modifier = Modifier.testTag(ForfeitButtonTag)
                    ) {
                        Text(text = stringResource(id = R.string.game_screen_forfeit))
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun GameFinishedLocalWinsScreenPreview() {
    GameScreen(
        players = listOf("AB", "CD"),
        state = GameScreenState(Game(), MatchState.FINISHED),
        result = HasWinner(Marker.LOCAL)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun GameFinishedLocalStartedScreenPreview() {
    GameScreen(
        players = listOf("AB", "CD"),
        state = GameScreenState(Game(), MatchState.STARTED),
        result = HasWinner(Marker.LOCAL)
    )
}