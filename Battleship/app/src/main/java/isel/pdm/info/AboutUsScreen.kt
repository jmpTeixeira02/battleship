package isel.pdm.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ui.topbar.NavigationTopBar
import isel.pdm.R
import isel.pdm.ui.topbar.NavigationHandlers
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun AboutUsScreen(
    navigationRequest: NavigationHandlers = NavigationHandlers(),
    sendEmailRequest: (() -> Unit) = {},
    authors: Iterable<String>
){
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag("AboutScreen"),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { NavigationTopBar(
                navigation = navigationRequest,
                title = stringResource( id = R.string.aboutUs_screenName)) }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    authors.forEach{
                        Text(text = it)
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                Icon(imageVector = Icons.Default.Email,
                    contentDescription = "Contact US",
                    modifier = Modifier.clickable(onClick = sendEmailRequest))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AboutUsScreenPreview(){
    AboutUsScreen(
        navigationRequest = NavigationHandlers(backRequest = {}),
        authors = listOf(
            "Author A - Axxxxx",
            "Author B - Axxxxx",
            "Author C - Axxxxx"
        )
    )
}