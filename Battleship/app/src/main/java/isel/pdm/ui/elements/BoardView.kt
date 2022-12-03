package isel.pdm.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.utils.drawCell

const val BOARD_SIDE: Int = 10

@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    cellFillColor: Color = Color.LightGray,
    cellText: String = " ",
    onClick: (x: Int, y: Int, boatColor: Color) -> Unit = {_,_,_ ->},
    selectedBoat: Int = -1,
    boardCellList: List<List<Color>> = List(BOARD_SIDE){ _ -> List(BOARD_SIDE){_ -> Color.LightGray} }
) {
    BoxWithConstraints(modifier = modifier) {
        val cellHeight = this.maxHeight / BOARD_SIDE
        val CellWidth = this.maxWidth / BOARD_SIDE
        Column()
        {
            repeat (BOARD_SIDE) { y ->
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    repeat (BOARD_SIDE) { x ->
                        val cellModifier = Modifier
                        drawCell(
                            modifier = cellModifier
                                .width(cellHeight)
                                .height(CellWidth),
                            boarderColor = boarderColor,
                            cellFillColor = boardCellList[y][x],
                            cellText = cellText,
                            onClick = {
                                if (selectedBoat != -1){
                                    onClick(x, y, Fleet.values()[selectedBoat].color)
                                }
                                else onClick(x, y, cellFillColor)
                            }
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