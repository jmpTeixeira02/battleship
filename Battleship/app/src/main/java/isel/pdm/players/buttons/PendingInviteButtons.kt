package isel.pdm.players.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R

@Composable
fun PendingInviteButtons(
    onAcceptInvite: () -> Unit = { },
    onDeleteInvite: () -> Unit = { },
) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        AcceptInvite(
            id = R.drawable.ic_flat_tick_icon,
            onClick = { onAcceptInvite() },
        )
        DeclineInvite(
            id = R.drawable.ic_flat_cross_icon,
            onClick = { onDeleteInvite() },
        )
    }
}

@Composable
fun AcceptInvite(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .padding(all = 8.dp)
            .clickable { onClick() }
    )
}

@Composable
fun DeclineInvite(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .padding(all = 8.dp)
            .clickable { onClick() }
    )
}

@Preview
@Composable
fun PendingInviteButtonsPreview() {
    PendingInviteButtons(
        onAcceptInvite = { },
        onDeleteInvite = { }
    )
}