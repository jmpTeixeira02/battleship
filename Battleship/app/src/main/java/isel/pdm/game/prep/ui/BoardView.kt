package isel.pdm.game.prep.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.game.prep.model.*

enum class CellColor(val color: Color) {
    Water(Color.LightGray),
    Destroyer(Color.Blue),
    Submarine(Color.Cyan),
    Cruiser(Color.Magenta),
    BattleShip(Color.Yellow),
    Carrier(Color.Green),
    ShotTaken(Color.DarkGray),
    Ship(Color.Red)
}

const val BoardTestTag = "BoardTag"

@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    cellText: String = " ",
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell(CellState.Water) } }
) {
    BoxWithConstraints(modifier = modifier.testTag(BoardTestTag)) {
        val cellHeight = this.maxHeight / BOARD_SIDE
        val cellWidth = this.maxWidth / BOARD_SIDE
        Column()
        {
            repeat(BOARD_SIDE) { line ->
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    repeat(BOARD_SIDE) { column ->

                        val cellModifier = Modifier
                        drawCell(
                            modifier = cellModifier
                                .width(cellHeight)
                                .height(cellWidth),
                            boarderColor = boarderColor,
                            cellFillColor = CellColor.valueOf(boardCellList[line][column].value!!).color,
                            cellText = cellText,
                            onClick = {
                                if (selectedBoat != null) onClick(line, column, Ship(selectedBoat))
                                else onClick(line, column, null)
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
fun BoardViewPreview() {
    BoardView()
}