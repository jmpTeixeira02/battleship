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
import isel.pdm.data.Replay
import isel.pdm.ui.elements.*
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun SelectReplayScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    availableReplays: List<Replay>,
    replayRequest: ReplayHandler = ReplayHandler()
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
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
                        ReplayView(
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
            Replay("#01", "01/01/0000", "OpponentX", 23),
            Replay("#02", "01/02/0000", "OpponentY", 18),
            Replay("#03", "02/01/0000", "OpponentZ", 31),
            Replay("#04", "01/01/3000", "OpponentW", 27),
            Replay("#05", "04/01/0000", "OpponentR", 22),
            Replay("#06", "05/05/5000", "OpponentT", 20),
        )
    )
}