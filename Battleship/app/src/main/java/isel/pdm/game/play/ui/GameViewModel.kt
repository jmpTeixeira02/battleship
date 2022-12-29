package isel.pdm.game.play.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.*
import isel.pdm.game.prep.model.BOARD_SIDE
import isel.pdm.game.prep.model.BiStateGameCellShot
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Coordinate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Represents the current match state
 */
enum class MatchState { IDLE, STARTING, STARTED, FINISHED }

/**
 * View model for the Game Screen hosted by [GameActivity].
 */
class GameViewModel(private val match: Match, myBoard: GameBoard, opponentBoard: GameBoard) : ViewModel() {


    private val _onGoingGame = MutableStateFlow(Game())
    val onGoingGame = _onGoingGame.asStateFlow()

    private var _state by mutableStateOf(MatchState.IDLE)
    val state: MatchState
        get() = _state

    fun startMatch(localPlayer: PlayerInfo, challenge: Challenge, gameBoard: GameBoard): Job? =
        if (state == MatchState.IDLE) {
            _state = MatchState.STARTING
            viewModelScope.launch {
                match.start(localPlayer, challenge, gameBoard).collect {
                    _onGoingGame.value = it.game
                    _state = when (it) {
                        is GameStarted -> MatchState.STARTED
                        is GameEnded -> MatchState.FINISHED
                        else ->
                            if (it.game.getResult() !is OnGoing) MatchState.FINISHED
                            else MatchState.STARTED
                    }

                    if (_state == MatchState.FINISHED)
                        match.end()
                }
            }
        }
        else null


    fun forfeit(): Job? =
        if (state == MatchState.STARTED) viewModelScope.launch { match.forfeit() }
        else null


    private val _myBoard = myBoard
    private val _myCells = _myBoard.cells
    val myCells = _myCells


    private val _opponentBoard = opponentBoard
    private val _opponentCells = _opponentBoard.cells
    val opponentCells = _opponentCells

    private var _winnerFound by mutableStateOf(false)
    val winnerFound: Boolean
        get() = _winnerFound


    fun opponentGameBoardClickHandler(line: Int, column: Int): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.takeOpponentBoardShot(Coordinate(line,column))
            }
        }
        else null


    fun localGameBoardClickHandler(line: Int, column: Int): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.takeLocalBoardShot(Coordinate(line,column))
            }
        }
        else null

    private fun checkIfWinnerExists(board: GameBoard): Boolean {
        repeat(BOARD_SIDE) { line ->
            repeat(BOARD_SIDE) { column ->
                if (board.cells[line][column].state == BiStateGameCellShot.HasNotBeenShot && board.cells[line][column].ship != null) return false
            }
        }
        return true
    }

}