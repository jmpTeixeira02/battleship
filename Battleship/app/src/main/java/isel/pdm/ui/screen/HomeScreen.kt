package isel.pdm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.activities.AboutUsActivity
import isel.pdm.ui.elements.TopBar
import isel.pdm.ui.theme.BattleshipTheme

/*data class PlayersListScreen(
    val players: List<PlayerView> = emptyList()
)*/

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
        ) { innerPadding ->
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                TODO()
            }

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

@Preview
@Composable
private fun PlayerInvitePreview () {

}