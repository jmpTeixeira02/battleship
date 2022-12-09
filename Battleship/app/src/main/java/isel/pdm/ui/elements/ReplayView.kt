package isel.pdm.ui.elements

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.data.game.Replay
import isel.pdm.R
import isel.pdm.ui.elements.buttons.ReplayButton
import isel.pdm.ui.theme.BattleshipTheme
import isel.pdm.ui.theme.Shapes

data class ReplayHandler(
    val onOpenSelectedReplay: (Replay) -> Unit = { }
)

/* UNUSED Composable -> REPLACED BY ExpandableReplayView */
@Composable
fun ReplayView(
    replay: Replay,
    replayRequest: ReplayHandler = ReplayHandler(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp,

    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = replay.replayId,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(all = 16.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = replay.date,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(all = 16.dp),
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                ReplayButton(
                    id = R.drawable.ic_replay_button_icon,
                    onClick = { replayRequest.onOpenSelectedReplay(replay) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SelectReplayScreenPreview() {
    BattleshipTheme {
        ReplayView(replay = Replay("#03", "22/10/2022", "OpponentX", emptyList()))
    }

}

