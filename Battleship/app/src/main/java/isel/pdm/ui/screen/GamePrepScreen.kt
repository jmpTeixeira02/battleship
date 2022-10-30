package isel.pdm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.pdm.R
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.elements.TopBar
import isel.pdm.ui.theme.BattleshipTheme
import java.time.format.TextStyle

@Composable
fun GamePrepScreen(
    players: Iterable<PlayerMatchmaking>
){
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
        ) { innerPadding ->
            HeaderPlayers(players)
        }
    }
}

@Composable
public fun HeaderPlayers(players: Iterable<PlayerMatchmaking>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primarySurface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        players.forEach{
            HeaderText(text = it.name)
        }
    }
}

@Composable
private fun HeaderText(text: String){
    Text(
        text = text,
        fontSize = 25.sp,
        maxLines = 1, color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun GamePrepScreen(){
    val players = listOf<PlayerMatchmaking>(PlayerMatchmaking("Player 1"), PlayerMatchmaking("Player 2"))
    GamePrepScreen(players)
}