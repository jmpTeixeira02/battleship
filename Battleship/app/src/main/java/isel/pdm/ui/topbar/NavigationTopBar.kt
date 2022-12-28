package isel.pdm.ui.topbar

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.ui.theme.BattleshipTheme

data class NavigationHandlers(
    val backRequest: (() -> Unit)? = null,
    val replayListRequest: (() -> Unit)? = null,
    val aboutUsRequest: (() -> Unit)? = null,
    val editUserRequest: (() -> Unit)? = null
)

// Test tags for the TopBar navigation elements
const val NavigateBackTestTag = "NavigateBack"
const val NavigateToReplayTestTag = "NavigateToReplay"
const val NavigateToAboutTestTag = "NavigateToAbout"
const val NavigateToEditUserTestTag = "NavigateToEditUser"


@Composable
fun NavigationTopBar(
    navigation: NavigationHandlers = NavigationHandlers(),
    title: String = ""
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (navigation.backRequest != null) {
                IconButton(
                    onClick = navigation.backRequest,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return to last screen")
                }
            }
        },
        actions = {
            if (navigation.replayListRequest != null) {
                IconButton(
                    onClick = navigation.replayListRequest,
                    modifier = Modifier.testTag(NavigateToReplayTestTag)
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Go to Replays Screen")
                }
            }
            if (navigation.editUserRequest != null) {
                IconButton(
                    onClick = navigation.editUserRequest,
                    modifier = Modifier.testTag(NavigateToEditUserTestTag)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Edit User Screen")
                }
            }
            if (navigation.aboutUsRequest != null) {
                IconButton(
                    onClick = navigation.aboutUsRequest,
                    modifier = Modifier.testTag(NavigateToAboutTestTag)
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Go to About US Screen")
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreviewHomeScreen() {
    BattleshipTheme {
        NavigationTopBar(NavigationHandlers(replayListRequest = {}, aboutUsRequest = {}), title = "Home Screen")
    }
}

@Preview
@Composable
private fun TopBarPreviewAboutUsScreen() {
    BattleshipTheme {
        NavigationTopBar(NavigationHandlers(backRequest = {}), title = "About US")
    }
}