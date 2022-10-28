package isel.pdm.ui.elements.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.data.players.InviteState


@Composable
fun InviteButton(
    state: InviteState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = state == InviteState.InviteEnabled,
        onClick = onClick,
        modifier = modifier
    ) {
        val text =
            if (state == InviteState.InviteEnabled)
                stringResource(id = R.string.enabled_invite_button_text)
            else
                stringResource(id = R.string.disabled_invite_button_text)
        Text(text = text)
    }
}

@Preview
@Composable
fun InviteButtonPreview() {
    InviteButton(
        state = InviteState.InviteEnabled,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}

@Preview
@Composable
fun InvitedButton() {
    InviteButton(
        state = InviteState.InvitedDisabled,
        onClick = { },
        modifier = Modifier.padding(all = 16.dp)
    )
}