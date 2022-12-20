package isel.pdm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.game.prep.model.*
import isel.pdm.game.prep.ui.drawCell


enum class BoardType { PrepBoard, GameBoard }

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
fun TriStateBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit = { _, _, _ -> },
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>>,
    gameColorPalette: Boolean,
    boardType: BoardType
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
                                .height(cellWidth),
                            boarderColor = boarderColor,
                            cellFillColor =
                            if (!gameColorPalette) {
                                CellColor.valueOf(boardCellList[line][column].prepCellValue).color
                            } else {
                                GameCellColor.valueOf(boardCellList[line][column].gameCellValue).color
                            },
                            cellText = if (boardType == BoardType.PrepBoard && boardCellList[line][column].state == BiStateGameCellShot.HasBeenShot) "X"
                            else " ",
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

@Composable
fun GamePrepBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    selectedBoat: TypeOfShip? = null,
    boardCellList: List<List<Cell>> = List(BOARD_SIDE) { _ -> List(BOARD_SIDE) { _ -> Cell() } }
) {
    TriStateBoard(
        modifier = modifier.testTag("GamePrepBoardTag"),
        boarderColor = boarderColor,
        onClick = onClick,
        selectedBoat = selectedBoat,
        boardCellList = boardCellList,
        gameColorPalette = false,
        boardType = BoardType.PrepBoard
    )
}

@Composable
fun MyGameBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    boardCellList: List<List<Cell>>
) {
    TriStateBoard(
        modifier = modifier.testTag("MyGameBoardTag"),
        boarderColor = boarderColor,
        onClick = onClick,
        boardCellList = boardCellList,
        gameColorPalette = false,
        boardType = BoardType.PrepBoard
    )
}

@Composable
fun OpponentGameBoard(
    modifier: Modifier = Modifier,
    boarderColor: Color = Color.Black,
    onClick: (line: Int, column: Int, selectedShip: Ship?) -> Unit,
    boardCellList: List<List<Cell>>
) {
    TriStateBoard(
        modifier = modifier.testTag("OpponentGameBoardTag"),
        boarderColor = boarderColor,
        onClick = onClick,
        boardCellList = boardCellList,
        gameColorPalette = true,
        boardType = BoardType.GameBoard
    )
}


@Preview
@Composable
fun GamePrepBoardPreview() {
    GamePrepBoard(onClick = {_, _, _ -> })
}