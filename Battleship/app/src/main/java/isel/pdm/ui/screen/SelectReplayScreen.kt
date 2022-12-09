package isel.pdm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.data.game.Replay
import isel.pdm.data.game.TurnManager
import isel.pdm.ui.elements.ExpandableReplayView
import isel.pdm.ui.elements.ReplayHandler
import isel.pdm.ui.elements.topbar.NavigationHandlers
import isel.pdm.ui.elements.ReplayView
import isel.pdm.ui.elements.topbar.NavigationTopBar
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun SelectReplayScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    availableReplays: List<Replay>,
    replayRequest: ReplayHandler = ReplayHandler(),
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                NavigationTopBar(
                    navigation = navigationRequest,
                    title = stringResource(id = R.string.replay_screenName)
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxHeight()) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1F)
                        .padding(innerPadding)
                ) {
                    items(availableReplays) {
                        ExpandableReplayView(
                            replay = it,
                            replayRequest = replayRequest,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun SelectReplayScreenPreview() {
    SelectReplayScreen(
        navigationRequest = NavigationHandlers(backRequest = {}),
        availableReplays = mutableListOf(
            Replay("#01", "01/01/0000", "OpponentX", listOf(TurnManager.fromString("E(0,6)"), TurnManager.fromString("P(3,9)"), TurnManager.fromString("E(5,3)"), TurnManager.fromString("P(9,9)"))),
            Replay("#02", "01/02/0000", "OpponentY", listOf(TurnManager.fromString("E(1,7)"), TurnManager.fromString("P(4,0)"), TurnManager.fromString("E(6,4)"), TurnManager.fromString("P(0,0)"))),
            Replay("#03", "02/01/0000", "OpponentZ", listOf(TurnManager.fromString("E(2,8)"), TurnManager.fromString("P(5,1)"), TurnManager.fromString("E(7,5)"), TurnManager.fromString("P(1,1)"))),
            Replay("#04", "01/01/3000", "OpponentW", listOf(TurnManager.fromString("E(3,9)"), TurnManager.fromString("P(6,2)"), TurnManager.fromString("E(8,6)"), TurnManager.fromString("P(2,2)"))),
            Replay("#05", "04/01/0000", "OpponentR", listOf(TurnManager.fromString("E(4,0)"), TurnManager.fromString("P(7,3)"), TurnManager.fromString("E(9,7)"), TurnManager.fromString("P(3,3)"))),
            Replay("#06", "05/05/5000", "OpponentT", listOf(TurnManager.fromString("E(5,1)"), TurnManager.fromString("P(8,4)"), TurnManager.fromString("E(0,8)"), TurnManager.fromString("P(4,4)"))),
        )
    )
}