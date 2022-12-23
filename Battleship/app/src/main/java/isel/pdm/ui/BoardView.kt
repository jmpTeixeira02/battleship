package isel.pdm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.game.prep.model.*


const val GamePrepBoardTag = "GamePrepBoardTag"

enum class CellColor(val color: Color) {
    Water(Color.LightGray),
    Destroyer(Color.Blue),
    Submarine(Color.Cyan),
    Cruiser(Color.Magenta),
    BattleShip(Color.Yellow),
    Carrier(Color.Green),
}

enum class GameCellColor(val color: Color) {
    Water(Color.LightGray),
    ShotTaken(Color.DarkGray),
    Ship(Color.Red)
}


@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    boarderColor: Color,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    selectedBoat: TypeOfShip? = null,
    cellText: (line: Int, column: Int) -> String = { _, _ -> " " },
    cellFillColor: (line: Int, column: Int) -> Color
) {

    BoxWithConstraints(modifier = modifier/*.testTag(BoardTestTag)*/) {
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
                                .height(cellWidth)
                                .testTag(BoardCellTestTag(line, column)),
                            boarderColor = boarderColor,
                            cellFillColor = cellFillColor(line, column),
                            cellText = cellText(line, column),
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

fun BoardCellTestTag(line: Int, column: Int): String {
    return "BoardCell${line}${column}Tag"
}

@Composable
fun GamePrepBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } }
) {
    BoardView(
        modifier = modifier.testTag(GamePrepBoardTag),
        boarderColor = boarderColor,
        onClick = onClick,
        selectedBoat = selectedBoat,
        cellFillColor = { line: Int, column: Int -> CellColor.valueOf(boardCellList[line][column].prepCellValue).color }
    )
}

@Composable
fun MyGameBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    boardCellList: List<List<Cell>>,
) {
    BoardView(
        modifier = modifier.testTag("MyGameBoardTag"),
        boarderColor = boarderColor,
        onClick = onClick,
        cellText = { line: Int, column: Int ->
            if (boardCellList[line][column].state == BiStateGameCellShot.HasBeenShot) "X"
            else " "
        },
        cellFillColor = { line: Int, column: Int -> CellColor.valueOf(boardCellList[line][column].prepCellValue).color }
    )
}

@Composable
fun OpponentGameBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    boardCellList: List<List<Cell>>
) {
    BoardView(
        modifier = modifier.testTag("OpponentGameBoardTag"),
        boarderColor = boarderColor,
        onClick = onClick,
        cellFillColor = { line: Int, column: Int -> GameCellColor.valueOf(boardCellList[line][column].gameCellValue).color }
    )
}


@Preview
@Composable
fun GamePrepBoardPreview() {
    GamePrepBoard(onClick = { _, _, _ -> })
}