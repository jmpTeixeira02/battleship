package isel.pdm.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.R
import isel.pdm.ui.theme.BattleshipTheme

@Composable
fun MainScreen(onStartRequested: () -> Unit) {
    BattleshipTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("MainScreen"),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_battleship_logo),
                    contentDescription = "",
                    modifier = Modifier.sizeIn(
                        100.dp, 100.dp,
                        250.dp, 250.dp
                    )
                )

                Button(
                    onClick = onStartRequested,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 120.dp, minHeight = 40.dp)
                        .testTag("PlayButton"),

                ) {
                    Text(
                        text = "Play"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(onStartRequested = { })
}