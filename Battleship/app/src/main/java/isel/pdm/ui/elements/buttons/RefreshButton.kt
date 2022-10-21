package isel.pdm.ui.elements.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.ui.theme.BattleshipTheme

enum class RefreshState { Ready, Refreshing }

@Composable
fun RefreshButton(
    state: RefreshState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = state == RefreshState.Ready,
        onClick = onClick,
        modifier = modifier.testTag(tag = "LoadingButton")
    ) {
        val text =
            if (state == RefreshState.Ready)
                stringResource(id = R.string.refreshButtonReady)
            else
                stringResource(id = R.string.refreshButtonRefreshing)
        Text(text = text)
    }
}

@Preview
@Composable
fun RefreshButtonPreviewReady() {
    BattleshipTheme {
        RefreshButton(
            state = RefreshState.Ready,
            onClick = { },
            modifier = Modifier.Companion.padding(all = 16.dp)
        )
    }
}

@Preview
@Composable
fun RefreshButtonPreviewRefreshing() {
    BattleshipTheme() {
        RefreshButton(
            state = RefreshState.Refreshing,
            onClick = { },
            modifier = Modifier.Companion.padding(all = 16.dp)
        )
    }
}
