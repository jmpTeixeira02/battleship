package isel.pdm.replay.selector.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.replay.selector.model.Replay


data class ReplayHandler(
    val onOpenSelectedReplay: (Replay) -> Unit = { }
)


@Composable
fun ExpandableReplayView(replay: Replay, replayRequest: ReplayHandler = ReplayHandler()) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    StatelessExpandableReplayView(
        replay = replay,
        isExpanded = isExpanded,
        onExpandedToggleRequest = { isExpanded = !isExpanded },
        replayRequest = replayRequest
    )
}

val expandedPropertyKey: SemanticsPropertyKey<Boolean> = SemanticsPropertyKey("Expanded")


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StatelessExpandableReplayView(
    replay: Replay,
    isExpanded: Boolean,
    onExpandedToggleRequest: () -> Unit = { },
    onSelected: () -> Unit = { },
    replayRequest: ReplayHandler = ReplayHandler()
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 10.dp,
        modifier = Modifier
            .testTag("ExpandableReplayView")
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { set(expandedPropertyKey, isExpanded) }
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = { replayRequest.onOpenSelectedReplay(replay) },
                    onLongClick = onExpandedToggleRequest
                )
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val maxLines: Int by animateIntAsState(
                    targetValue = if (isExpanded) 10 else 1,
                    animationSpec = tween(
                        delayMillis = 50,
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
                Text(
                    text = "Game ID: " + replay.replayId +
                            "\n\nOpponent name: " + replay.opponentName +
                            "\n\nShots fired: " + replay.shotsFired.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    maxLines = maxLines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                )

                val icon =
                    if (isExpanded) Icons.Default.ArrowDropUp
                    else Icons.Default.ArrowDropDown
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onExpandedToggleRequest)
                        .testTag("ExpandableReplayView.ExpandAction")
                )
            }
            Text(
                text = replay.date,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedReplayViewPreview() {
    StatelessExpandableReplayView(replay = randomReplay, isExpanded = true)
}


@Preview(showBackground = true)
@Composable
private fun CollapsedReplayViewPreview() {
    StatelessExpandableReplayView(replay = randomReplay, isExpanded = false)
}

@Preview(showBackground = true)
@Composable
private fun ExpandableReplayViewPreview() {
    ExpandableReplayView(replay = randomReplay)
}

private val randomReplay = Replay(
    replayId = "#XPTO123",
    date = "14/10/2022",
    opponentName = "ADV_3",
    shotsFired = 29
)