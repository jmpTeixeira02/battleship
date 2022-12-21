package isel.pdm.game.play.ui

import androidx.lifecycle.ViewModel
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.*


class GameViewModel(myBoard: GameBoard, opponentBoard: GameBoard) : ViewModel() {

    private val _myBoard = myBoard
    private val _myCells = _myBoard.cells
    val myCells = _myCells


    private val _opponentBoard = opponentBoard
    private val _opponentCells = _opponentBoard.cells
    val opponentCells = _opponentCells


    fun gameBoardClickHandler(line: Int, column: Int) {
        if (_myCells[line][column].state == BiStateGameCellShot.HasBeenShot) return
        else takeShot(line, column)
    }

    private fun takeShot(line: Int, column: Int) {
        try {

            val shipHit: Boolean = _myBoard.takeShot(Coordinate(line, column))

            if (!shipHit) _myBoard.turn.other

        } catch (e: Exception) {
            throw e
        }
    }

}