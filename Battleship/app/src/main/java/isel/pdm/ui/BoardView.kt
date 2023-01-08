package isel.pdm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.game.prep.model.*


const val GamePrepBoardTag = "GamePrepBoardTag"
const val MyGameBoard = "MyGameBoardTag"
const val OpponentGameBoard = "OpponentGameBoardTag"

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
private fun BoardView(
    modifier: Modifier = Modifier,
    boarderColor: Color,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    selectedBoat: TypeOfShip? = null,
    cellText: (line: Int, column: Int) -> String = { _, _ -> " " },
    cellFillColor: (line: Int, column: Int) -> Color,
    enabled: Boolean = false
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
                            },
                            enabled = enabled
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

fun OpponentBoardCellTestTag(line: Int = 0, column: Int = 0): String {
    return "OpponentBoardCell${line}${column}Tag"
}

@Composable
fun GamePrepBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } },
    enabled: Boolean
) {
    BoardView(
        modifier = modifier.testTag(GamePrepBoardTag),
        boarderColor = boarderColor,
        onClick = onClick,
        selectedBoat = selectedBoat,
        cellFillColor = { line: Int, column: Int -> CellColor.valueOf(boardCellList[line][column].prepCellValue).color },
        enabled = enabled
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
        modifier = modifier.testTag(MyGameBoard),
        boarderColor = boarderColor,
        onClick = onClick,
        cellText = { line: Int, column: Int ->
            if (boardCellList[line][column].state == BiStateGameCellShot.HasBeenShot) "X"
            else " "
        },
        cellFillColor = { line: Int, column: Int -> CellColor.valueOf(boardCellList[line][column].prepCellValue).color },
    )
}

@Composable
fun OpponentGameBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    boardCellList: List<List<Cell>>,
    enabled: Boolean
) {
    BoardView(
        modifier = modifier.testTag(OpponentBoardCellTestTag()),
        boarderColor = boarderColor,
        onClick = onClick,
        cellFillColor = { line: Int, column: Int -> GameCellColor.valueOf(boardCellList[line][column].gameCellValue).color },
        enabled = enabled
    )
}


@Preview
@Composable
fun GamePrepBoardPreview() {
    GamePrepBoard(onClick = { _, _, _ -> }, enabled = true)
}