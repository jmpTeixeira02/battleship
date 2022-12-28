package isel.pdm.game.play.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.play.model.Marker
import isel.pdm.game.prep.model.*
import kotlin.random.Random


class GameViewModel(myBoard: GameBoard, opponentBoard: GameBoard) : ViewModel() {

    private val _myBoard = myBoard
    private val _myCells = _myBoard.cells
    val myCells = _myCells


    private val _opponentBoard = opponentBoard
    private val _opponentCells = _opponentBoard.cells
    val opponentCells = _opponentCells

    private var _isTurn by mutableStateOf(Marker.LOCAL)
    val isTurn: Marker
        get() = _isTurn

    private var _winnerFound by mutableStateOf(false)
    val winnerFound: Boolean
        get() = _winnerFound

    fun opponentGameBoardClickHandler(line: Int, column: Int) {
        if (checkLocalBoardCellAlreadyPlayed(line, column, _opponentCells)) return
        else shootOpponentBoard(line, column)
    }

    private fun shootOpponentBoard(line: Int, column: Int) {
        try {

            val shipHit: Boolean = _opponentBoard.shoot(Coordinate(line, column))

            if (!shipHit) {
                _isTurn = _isTurn.other

                // For fake opponent only
                val randomCords = generateFakeCords()
                localGameBoardClickHandler(randomCords.line, randomCords.column)
            } else {
                if (checkIfWinnerExists(_opponentBoard)) {
                    _winnerFound = true
                    return
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }


    private var _randomPossibleCordsList by mutableStateOf(mutableListOf<Coordinate>())
    private var _isNextBestCord by mutableStateOf(Coordinate(0, 0))

    fun localGameBoardClickHandler(line: Int, column: Int) {

        // For fake opponent only
        if (_randomPossibleCordsList.isNotEmpty()) {

            _isNextBestCord = getNextBestPossibleCords()

            if (checkLocalBoardCellAlreadyPlayed(
                    _isNextBestCord.line,
                    _isNextBestCord.column,
                    _myCells
                )
            ) {
                if (_randomPossibleCordsList.isEmpty()) {
                    val randomCords = generateFakeCords()
                    localGameBoardClickHandler(randomCords.line, randomCords.column)
                } else {
                    _isNextBestCord = getNextBestPossibleCords()
                    localGameBoardClickHandler(_isNextBestCord.line, _isNextBestCord.column)
                }
            } else shootLocalBoard(_isNextBestCord.line, _isNextBestCord.column)

        } else {
            if (checkLocalBoardCellAlreadyPlayed(line, column, _myCells)) {

                // For fake opponent only
                val randomCords = generateFakeCords()
                localGameBoardClickHandler(randomCords.line, randomCords.column)

            } else shootLocalBoard(line, column)
        }


    }

    private fun shootLocalBoard(line: Int, column: Int) {
        try {
            val shipHit: Boolean = _myBoard.shoot(Coordinate(line, column))

            if (!shipHit) {
                _isTurn = _isTurn.other
            } else {
                if (checkIfWinnerExists(_myBoard)) {
                    _winnerFound = true
                    return
                }

                // For fake opponent only
                val randomCords = generateSmartFakeCords(line, column)
                localGameBoardClickHandler(randomCords.line, randomCords.column)

            }

        } catch (e: Exception) {
            throw e
        }
    }


    private fun checkIfWinnerExists(board: GameBoard): Boolean {
        repeat(BOARD_SIDE) { line ->
            repeat(BOARD_SIDE) { column ->
                if (board.cells[line][column].state == BiStateGameCellShot.HasNotBeenShot && board.cells[line][column].ship != null) return false
            }
        }
        return true
    }

    private fun checkLocalBoardCellAlreadyPlayed(
        line: Int,
        column: Int,
        boardCells: MutableList<MutableList<Cell>>
    ): Boolean = boardCells[line][column].state == BiStateGameCellShot.HasBeenShot


    private fun generateFakeCords() = Coordinate((0..9).random(), (0..9).random())

    private fun generateSmartFakeCords(line: Int, column: Int): Coordinate {
        val randomizer = Random

        when (line) {
            0 -> {
                _randomPossibleCordsList.add(Coordinate(1, column))
                checkColumnCorners(line, column, _randomPossibleCordsList)
            }
            9 -> {
                _randomPossibleCordsList.add(Coordinate(line - 1, column))
                checkColumnCorners(line, column, _randomPossibleCordsList)
            }
            else -> {
                _randomPossibleCordsList.add(Coordinate(line + 1, column))
                _randomPossibleCordsList.add(Coordinate(line - 1, column))
                checkColumnCorners(line, column, _randomPossibleCordsList)
            }
        }

        return _randomPossibleCordsList[randomizer.nextInt(_randomPossibleCordsList.size)]

    }


    private fun checkColumnCorners(
        line: Int,
        column: Int,
        randomCordsList: MutableList<Coordinate>
    ) {
        if (column > 0) randomCordsList.add(Coordinate(line, column - 1))
        if (column < 9) randomCordsList.add(Coordinate(line, column + 1))
    }


    private fun getNextBestPossibleCords(): Coordinate =
        _randomPossibleCordsList.removeAt(Random.nextInt(_randomPossibleCordsList.size))

    /*private fun checkLineCorners(line: Int, column: Int, randomCordsList: MutableList<Coordinate>) {
        if(line > 0) randomCordsList.add(Coordinate(line - 1, column))
        if(line < 9) randomCordsList.add(Coordinate(line + 1, column))
    }*/

}