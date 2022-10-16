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

@Composable
fun TopBar(
    backRequest: (() -> Unit)? = null,
    aboutUsRequest: (() -> Unit)? = null,
    replayRequest: (() -> Unit)? = null,
    title: String = ""
){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (backRequest != null){
                IconButton(onClick = backRequest) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return to last screen")
                }
            }
        },
        actions = {
            if (replayRequest != null){
                IconButton(onClick = replayRequest) {
                    Icon(Icons.Default.Star, contentDescription = "Go to Replays Screen")
                }
            }
            if (aboutUsRequest != null){
                IconButton(onClick = aboutUsRequest) {
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
        TopBar(replayRequest = {}, aboutUsRequest = {}, title = "Home Screen")
    }
}

@Preview
@Composable
private fun TopBarPreviewAboutUsScreen(){
    BattleshipTheme {
        TopBar(backRequest = {}, title = "About US")
    }
}