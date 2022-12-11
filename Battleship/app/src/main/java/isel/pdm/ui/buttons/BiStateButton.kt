package isel.pdm.ui.buttons

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


class BiStateButtonStrings(val hasNotBeenPressedStr: String, val hasBeenPressedStrResStr: String)

enum class BiState {hasNotBeenPressed, hasBeenPressed}

@Composable
private fun BiStateButton(
    state: BiState,
    onClick: () -> Unit,
    biStateButtonStrings: BiStateButtonStrings,
    modifier: Modifier = Modifier,
    toggle: Boolean = false
) {
    Button(
        enabled =
            if (!toggle) state == BiState.hasNotBeenPressed
            else true,
        onClick = onClick,
        modifier = modifier
    ) {
        val text =
            if (state == BiState.hasNotBeenPressed)
                biStateButtonStrings.hasNotBeenPressedStr
            else
                biStateButtonStrings.hasBeenPressedStrResStr
        Text(text = text)
    }
}

@Composable
fun RefreshButton(
    state: BiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    BiStateButton(
        state = state,
        onClick = onClick,
        modifier = modifier.testTag("LoadingButton"),
        biStateButtonStrings = BiStateButtonStrings(
            hasNotBeenPressedStr = stringResource(id = R.string.refreshButtonReady),
            hasBeenPressedStrResStr = stringResource(id = R.string.refreshButtonRefreshing),
        )
    )
}

@Composable
fun InviteButton(
    state: BiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    BiStateButton(
        state = state,
        onClick = onClick,
        modifier = modifier,
        biStateButtonStrings = BiStateButtonStrings(
            hasNotBeenPressedStr = stringResource(id = R.string.enabled_invite_button_text),
            hasBeenPressedStrResStr = stringResource(id = R.string.disabled_invite_button_text),
        )
    )
}

@Composable
fun RemoveBoatButton(
    state: BiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    BiStateButton(
        state = state,
        onClick = onClick,
        modifier = modifier,
        toggle = true,
        biStateButtonStrings = BiStateButtonStrings(
            hasNotBeenPressedStr= stringResource(id = R.string.boatDeleteButtonReady),
            hasBeenPressedStrResStr = stringResource(id = R.string.boatDeleteButtonPending),
        )
    )
}

@Preview
@Composable
fun RefreshButtonPreviewReady() {
    BattleshipTheme {
        RefreshButton(
            state = BiState.hasNotBeenPressed,
            onClick = { },
            modifier = Modifier.padding(all = 16.dp)
        )
    }
}

@Preview
@Composable
fun RefreshButtonPreviewRefreshing() {
    BattleshipTheme {
        RefreshButton(
            state = BiState.hasBeenPressed,
            onClick = { },
            modifier = Modifier.padding(all = 16.dp)
        )
    }
}

@Preview
@Composable
fun InviteButtonPreviewInvitePlayer() {
    InviteButton(
        state = BiState.hasNotBeenPressed,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}

@Preview
@Composable
fun InvitedButtonPlayerHasBeenInvited() {
    InviteButton(
        state = BiState.hasBeenPressed,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}

@Preview
@Composable
fun RemoveBoatButtonHasNotBeenPressed() {
    RemoveBoatButton(
        state = BiState.hasNotBeenPressed,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}

@Preview
@Composable
fun RemoveBoatButtonHasBeenPressed() {
    RemoveBoatButton(
        state = BiState.hasBeenPressed,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}
