package isel.pdm.ui.elements.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.pdm.data.PlayerMatchmaking

@Composable
fun GameTopBar(players: Iterable<PlayerMatchmaking>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primarySurface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        players.forEach{
            GameTopBarText(text = it.name)
        }
    }
}

@Composable
private fun GameTopBarText(text: String){
    Text(
        text = text,
        fontSize = 25.sp,
        maxLines = 1, color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun GameTopBarPreview(){
    GameTopBar(players = listOf(
        PlayerMatchmaking("Player 1"),
        PlayerMatchmaking("Player 2")
    ))
}