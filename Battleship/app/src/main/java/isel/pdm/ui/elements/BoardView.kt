package isel.pdm.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.utils.drawCell

const val BOARD_SIDE: Int = 10

enum class BoardCell(val color: Color){
    Water(Color.LightGray),
    Destroyer(Color.Blue),
    Submarine(Color.Cyan),
    Cruiser(Color.Magenta),
    BattleShip(Color.Yellow),
    Carrier(Color.Green)
}

@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    cellFillColor: Color = BoardCell.Water.color,
    cellText: String = " ",
    onClick: (x: Int, y: Int, selectedBoat: BoardCell) -> Unit = {_,_,_ ->},
    selectedBoat: Int = -1,
    boardCellList: List<List<BoardCell>> = List(BOARD_SIDE){ _ -> List(BOARD_SIDE){_ -> BoardCell.Water} }
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
                            cellFillColor = boardCellList[y][x].color,
                            cellText = cellText,
                            onClick = {
                                if (selectedBoat != -1){
                                    // Add one because there is water
                                    onClick(x, y, BoardCell.values()[selectedBoat+1])
                                }
                                else onClick(x, y, BoardCell.Water)
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