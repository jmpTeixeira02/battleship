package isel.pdm.game.play.ui

import androidx.lifecycle.ViewModel
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.*


class GameViewModel : ViewModel() {

    private val _gameBoard = GameBoard()

    private val _gameBoardCells = _gameBoard.cells
    val gameBoardCells = _gameBoardCells


    fun gameBoardClickHandler(line: Int, column: Int) {
        if (_gameBoardCells[line][column].state == BiStateGameCellShot.HasBeenShot) return
        else takeShot(line, column)
    }

    private fun takeShot(line: Int, column: Int) {
        try {

            val shipHit: Boolean = _gameBoard.takeShot(Coordinate(line, column))
           // _gameBoardCells[line][column] = Cell(CellState.ShotTaken)

            if (!shipHit) _gameBoard.turn.other

        } catch (e: Exception) {
            throw e
        }
    }

}