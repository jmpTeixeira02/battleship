package isel.pdm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.ui.elements.BoardView
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.ui.elements.buttons.RemoveBoatButton
import isel.pdm.ui.elements.buttons.ReplayButton
import isel.pdm.ui.elements.topbar.GameTopBar
import isel.pdm.ui.theme.BattleshipTheme
import java.time.format.TextStyle

@Composable
fun GamePrepScreen(
    players: List<PlayerMatchmaking>,
    rotateBoat: () -> Unit = {},
    deleteBoat: () -> Unit,
    deleteBoatState: BiState = BiState.hasNotBeenPressed
){
    BattleshipTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = {GameTopBar(players)}
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                BoardView(modifier = Modifier
                    .width(248.dp)
                    .height(248.dp))
                Button(onClick = rotateBoat, modifier = Modifier.padding(all = 16.dp)) {
                    Text(text = "Random")
                }
                RemoveBoatButton(state = deleteBoatState, onClick = deleteBoat)
            }
        }
    }
}

@Preview
@Composable
private fun GamePrepScreenPreview(){
    GamePrepScreen(
        players = listOf(
            PlayerMatchmaking("Player 1"),
            PlayerMatchmaking("Player 2")
        ),
        deleteBoat = {}
    )
}