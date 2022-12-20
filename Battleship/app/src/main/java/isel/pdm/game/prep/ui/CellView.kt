package isel.pdm.game.prep.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun drawCell(
    modifier: Modifier = Modifier,
    boarderColor: Color,
    cellFillColor: Color,
    cellText: String = " ",
    onClick: () -> Unit = {}
){
    Box(modifier = modifier
        .border(width = 1.dp, color = boarderColor)
        .background(cellFillColor)
        .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ){
        Text(
            text = cellText,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun drawCellPreview(){
    drawCell(
        boarderColor = Color.Black,
        cellFillColor = Color.Red,
        modifier = Modifier
            .width(128.dp)
            .height(128.dp)
    )
}