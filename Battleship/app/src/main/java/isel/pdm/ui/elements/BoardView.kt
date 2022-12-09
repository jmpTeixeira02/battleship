package isel.pdm.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import isel.pdm.utils.drawCell


@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    size: Int = 10,
    boarderColor: Color = Color.Black,
    cellFillColor: Color = Color.LightGray,
    cellText: String = " ",
    onClick: (x: Int, y: Int) -> Unit = {_,_ ->}
) {
    BoxWithConstraints(modifier = modifier) {
        val cellHeight = this.maxHeight / size
        val CellWidth = this.maxWidth / size
        Column()
        {
            for (x in 1..10) {
                Row(
                    modifier = Modifier
                        .background(color = cellFillColor),
                    horizontalArrangement = Arrangement.Start
                ) {
                    for (y in 1..size) {
                        drawCell(
                            modifier = Modifier
                                .width(cellHeight)
                                .height(CellWidth),
                            boarderColor = boarderColor,
                            cellFillColor = cellFillColor,
                            cellText = cellText,
                            onClick = { onClick(x, y)}
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BoardViewPreview(){
    BoardView()
}