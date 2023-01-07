package isel.pdm.replay.selector.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.replay.selector.model.Replay
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.topbar.NavigationTopBar

const val SelectReplayScreenTag = "SelectReplayScreen"

@Composable
fun SelectReplayScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    availableReplays: List<Replay>,
    replayRequest: ReplayHandler = ReplayHandler(),
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(SelectReplayScreenTag),
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
            Replay(opponentName = "opponent", shotsFired = 1),
            Replay(opponentName = "opponent1", shotsFired = 2),
            Replay(opponentName = "opponent2", shotsFired = 3),
            Replay(opponentName = "opponent3", shotsFired = 4),
        )
    )
}