package isel.pdm.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.R
import isel.pdm.activities.AboutUsActivity
import isel.pdm.ui.elements.TopBar
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun HomeScreen(
    backRequest: (() -> Unit)? = null,
    aboutUsRequest: (() -> Unit)? = null,
    replayRequest: (() -> Unit)? = null
){
    BattleshipTheme{
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    backRequest = backRequest,
                    aboutUsRequest = aboutUsRequest,
                    replayRequest = replayRequest,
                    title = stringResource(id = R.string.app_name)
                )
            }
        ){

        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(){
    HomeScreen(
        aboutUsRequest = {},
        replayRequest = {}
    )
}