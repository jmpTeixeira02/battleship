package isel.pdm.ui.elements.topbar

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.ui.theme.BattleshipTheme

data class NavigationHandlers(
    val backRequest: (() -> Unit)? = null,
    val replayRequest: (() -> Unit)? = null,
    val aboutUsRequest: (() -> Unit)? = null,
)

@Composable
fun NavigationTopBar(
    navigation: NavigationHandlers = NavigationHandlers(),
    title: String = ""
){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (navigation.backRequest != null){
                IconButton(onClick = navigation.backRequest) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return to last screen")
                }
            }
        },
        actions = {
            if (navigation.replayRequest != null){
                IconButton(onClick = navigation.replayRequest) {
                    Icon(Icons.Default.Star, contentDescription = "Go to Replays Screen")
                }
            }
            if (navigation.aboutUsRequest != null){
                IconButton(onClick = navigation.aboutUsRequest) {
                    Icon(Icons.Default.Info, contentDescription = "Go to About US Screen")
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreviewHomeScreen(){
    BattleshipTheme {
        NavigationTopBar(NavigationHandlers(replayRequest = {}, aboutUsRequest = {}), title = "Home Screen")
    }
}

@Preview
@Composable
private fun TopBarPreviewAboutUsScreen(){
    BattleshipTheme {
        NavigationTopBar(NavigationHandlers(backRequest = {}), title = "About US")
    }
}