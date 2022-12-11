package isel.pdm.replay.viewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.replay.selector.model.Replay
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.topbar.NavigationTopBar
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun ReplayGameScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    replay: Replay
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                NavigationTopBar(
                    navigation = navigationRequest,
                    title = replay.replayId
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight()
            ) {
                ReplayGameView(replay = replay)

                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    /*
                            BOARD VIEW
                     */

                    Button(onClick = { }) { // É necessário andar play para trás?
                        Text(text = "<")
                    }
                    Button(onClick = { }) {
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
    shotsFired = 17
)