package isel.pdm.game.prep.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip

enum class CellColor(val color: Color){
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
    cellText: String = " ",
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ ->},
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>> = List(BOARD_SIDE){ _ -> List(BOARD_SIDE){ _ -> Cell(null) } }
) {
    BoxWithConstraints(modifier = modifier) {
        val cellHeight = this.maxHeight / BOARD_SIDE
        val CellWidth = this.maxWidth / BOARD_SIDE
        Column()
        {
            repeat (BOARD_SIDE) { line ->
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    repeat (BOARD_SIDE) { column ->

                        val cellModifier = Modifier
                        drawCell(
                            modifier = cellModifier
                                .width(cellHeight)
                                .height(CellWidth),
                            boarderColor = boarderColor,
                            cellFillColor = CellColor.valueOf(boardCellList[line][column].value).color,
                            cellText = cellText,
                            onClick = {
                                if (selectedBoat != null) onClick(line, column, Ship(selectedBoat))
                                else onClick(line,column,null)
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