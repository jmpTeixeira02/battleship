package isel.pdm.ui.elements

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.R
import isel.pdm.ui.theme.BattleshipTheme

data class NavigationHandlers(
    val backRequest: (() -> Unit)? = null,
    val replayRequest: (() -> Unit)? = null,
    val aboutUsRequest: (() -> Unit)? = null,
)

@Composable
fun TopBar(
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
        TopBar(NavigationHandlers(replayRequest = {}, aboutUsRequest = {}), title = "Home Screen")
    }
}

@Preview
@Composable
private fun TopBarPreviewAboutUsScreen(){
    BattleshipTheme {
        TopBar(NavigationHandlers(backRequest = {}), title = "About US")
    }
}