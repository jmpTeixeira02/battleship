package isel.pdm.preferences.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.lobby.model.userNameOrNull
import isel.pdm.ui.IsReadOnly
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.topbar.NavigationTopBar
import isel.pdm.ui.buttons.EditFab
import isel.pdm.ui.buttons.FabMode
import isel.pdm.ui.theme.BattleshipTheme

/*data class PlayerHandler(
    val onCreatePlayer: (PlayerMatchmaking) -> Unit = { },
)*/

const val CreatePlayerScreenTag = "CreatePlayerScreen"
const val UsernameInputTag = "UsernameInput"

@Composable
fun CreatePlayerScreen(
    player: PlayerMatchmaking?,
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    //playerRequest: PlayerHandler = PlayerHandler(),
    onSaveRequested: (PlayerMatchmaking) -> Unit = { }
) {

    BattleshipTheme {

        var displayedUsername by remember { mutableStateOf(player?.username ?: "") }
        var editing by remember { mutableStateOf(player == null) }

        val enteredUsername = userNameOrNull(username = displayedUsername)

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(CreatePlayerScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                NavigationTopBar(
                    navigation = navigationRequest,
                    title = stringResource(id = R.string.app_name)
                )
            },
            floatingActionButton = {
                EditFab(
                    if (!editing) {
                        { editing = true }
                    } else if (enteredUsername == null) null
                    else {
                        { onSaveRequested(enteredUsername) }
                    },
                    mode = if (editing) FabMode.Save else FabMode.Edit
                )
            }
        )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                //var textState by rememberSaveable { mutableStateOf("") }
                TextField(
                    singleLine = true,
                    value = displayedUsername,
                    onValueChange = { if (it.length <= 15) displayedUsername = it.trim() },
                    label = {
                        Text("Your username")
                    },
                    readOnly = !editing,
                    modifier = Modifier
                        .padding(all = 32.dp)
                        .testTag(UsernameInputTag)
                        .semantics { if (!editing) this[IsReadOnly] = Unit },

                    )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenViewModePreview() {
    CreatePlayerScreen(
        player = PlayerMatchmaking("my user"),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenEditModePreview() {
    CreatePlayerScreen(
        player = null,
    )
}