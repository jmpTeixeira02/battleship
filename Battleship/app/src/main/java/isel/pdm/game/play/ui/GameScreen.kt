package isel.pdm.game.play.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.game.play.model.BoardResult
import isel.pdm.game.play.model.Game
import isel.pdm.game.play.model.HasWinner
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.GameTopBar
import isel.pdm.R
import isel.pdm.game.play.model.Marker
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.game.prep.ui.FleetSelectorView
import isel.pdm.game.prep.ui.ShipState

val PREVIEW_MY_GAME_BOARD_SIZE: Dp = 160.dp
val OPPONENT_GAME_BOARD_SIZE: Dp = 260.dp

internal const val ForfeitButtonTag = "ForfeitButton"
internal const val FavoritesButtonTag = "FavoritesButton"

data class GameScreenState(
    val game: Game,
    val matchState: MatchState
)

const val GameScreenTestTag = "GameScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameScreen(
    players: List<String>,
    state: GameScreenState,
    boardCellHandler: BoardCellHandler = BoardCellHandler(),
    onForfeitRequested: () -> Unit = { },
    onAddToFavoritesRequested: () -> Unit = { },
    result: BoardResult,
    destroyedShips: Map<TypeOfShip, ShipState> = TypeOfShip.values()
        .associateWith { _ -> ShipState.isNotSelected },
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


                if (state.matchState == MatchState.FINISHED) {
                    Row(
                        //modifier = Modifier.padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.game_screen_add_to_favourites),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Button(
                            onClick = onAddToFavoritesRequested,
                            modifier = Modifier
                                .testTag(FavoritesButtonTag)
                                .size(width = 64.dp, height = 32.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.game_screen_accept_additon_to_favourites),
                            )
                        }
                    }

                }

                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primaryVariant
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

@Preview
@Composable
fun GameFinishedLocalWinsScreenPreview() {
    GameScreen(
        players = listOf("AB", "CD"),
        state = GameScreenState(Game(), MatchState.FINISHED),
        result = HasWinner(Marker.LOCAL)
    )
}