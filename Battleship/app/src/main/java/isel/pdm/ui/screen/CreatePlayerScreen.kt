package isel.pdm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.elements.TopBar
import isel.pdm.ui.theme.BattleshipTheme

data class PlayerHandler(
    val onCreatePlayer: (PlayerMatchmaking) -> Unit = { },
)


@Composable
fun CreatePlayerScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    playerRequest: PlayerHandler = PlayerHandler(),
) {

    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    navigation = navigationRequest,
                    title = stringResource(id = R.string.app_name)
                )
            }
        )
        { _ ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                var textState by rememberSaveable { mutableStateOf("") }
                TextField(
                    singleLine = true,
                    value = textState,
                    onValueChange = { if (it.length <= 8) textState = it },
                    modifier = Modifier.padding(all = 32.dp)
                )
                Button(
                    onClick = { playerRequest.onCreatePlayer(PlayerMatchmaking(textState)) }
                ) {
                    Text(
                        text = "Create player"
                    )
                }
            }

        }
    }
}
