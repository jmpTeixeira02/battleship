package isel.pdm.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.utils.drawCell


@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    BoxWithConstraints(modifier = modifier){
        val boxWithConstraintsScope = this
        Column()
        {
            for (i in 1..10){
                drawRowOfCells(
                    size = 10,
                    boxWithConstraintsScope = boxWithConstraintsScope,
                    boarderColor = Color.Black,
                    cellFillColor = Color.LightGray,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun drawRowOfCells(
    size: Int,
    boxWithConstraintsScope: BoxWithConstraintsScope,
    boarderColor: Color,
    cellFillColor: Color,
    cellText: String = " ",
    onClick: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .background(color = cellFillColor),
        horizontalArrangement = Arrangement.Start
    ){
        for (k in 1.. size){
            drawCell(
                modifier = Modifier
                    .width(boxWithConstraintsScope.maxWidth / size)
                    .height(boxWithConstraintsScope.maxHeight / size),
                boarderColor = boarderColor,
                cellFillColor = cellFillColor,
                cellText = cellText,
                onClick = onClick
            )
        }
    }
}


@Preview
@Composable
fun BoardViewPreview(){
    BoardView()
}