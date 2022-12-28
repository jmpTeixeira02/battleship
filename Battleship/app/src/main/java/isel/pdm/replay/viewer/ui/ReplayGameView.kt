package isel.pdm.replay.viewer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import isel.pdm.replay.selector.model.Replay

@Composable
fun ReplayGameView(replay: Replay) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = "Opponent: ",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start
        )
        Text(
            text = replay.opponentName,
            style = MaterialTheme.typography.h6
        )
        Text(
            text = replay.date,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = "Shots fired: ",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 8.dp),
            textAlign = TextAlign.Start
        )
        Text(
            text = replay.shotsFired.toString(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 8.dp),
        )
    }
}