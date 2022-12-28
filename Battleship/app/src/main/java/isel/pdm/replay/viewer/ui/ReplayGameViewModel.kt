package isel.pdm.replay.viewer.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.BiStateGameCellShot
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Coordinate
import isel.pdm.replay.selector.model.GameInfo
import isel.pdm.replay.selector.model.Replay

class ReplayGameViewModel(
    replay: Replay,
) : ViewModel() {
    val gameInfo = replay.gameInfo

    private val _myCells = gameInfo.myBoard.cells
    private val _opponentCells = gameInfo.opponentBoard.cells

    private val _myReplayCells = replay.gameInfo.myBoard.cells
    val myReplayCells = _myReplayCells

    private val _opponentReplayCells = GameBoard().cells
    val opponentReplayCells = _opponentReplayCells

    private var _moveCounter by mutableStateOf(0)
    val moveCounter: Int
        get() = _moveCounter

    fun moveForward(){
        if (_moveCounter >= gameInfo.myMoves.size + gameInfo.opponentMoves.size){
            return
        }
        makeMove(
            myMove = {cord -> _opponentCells[cord.line][cord.column]},
            opponentMove = BiStateGameCellShot.HasBeenShot
        )
        _moveCounter++
    }

    fun moveBackward(){
        if (_moveCounter <= 0){
            return
        }
        _moveCounter--
        makeMove(
            myMove = {_ -> Cell() },
            opponentMove = BiStateGameCellShot.HasNotBeenShot
        )
    }

    private val myTurn = if (gameInfo.iMadeFirstMove) 0 else 1

    // It's updating but not in the UI
    private fun makeMove(myMove: (coordinate: Coordinate) -> Cell, opponentMove: BiStateGameCellShot){
        if (_moveCounter % 2 == myTurn){ // My Move
            Log.v("ReplayGameVM", "OWN MOVE")
            val coordinate = gameInfo.myMoves[_moveCounter / 2]
            _opponentReplayCells[coordinate.line][coordinate.column] = myMove(coordinate)
        }
        else{ // Opponent Move
            val coordinate = gameInfo.opponentMoves[_moveCounter / 2]
            _myReplayCells[coordinate.line][coordinate.column].state = opponentMove
        }
    }
}