package isel.pdm .game.play.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.*
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.Coordinate
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.game.prep.ui.ShipState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * Represents the current match state
 */
enum class MatchState { IDLE, STARTING, STARTED, FINISHED }

/**
 * View model for the Game Screen hosted by [GameActivity].
 */
class GameViewModel(private val match: Match, myBoard: GameBoard) : ViewModel() {


    private val _onGoingGame = MutableStateFlow(Game())
    val onGoingGame = _onGoingGame.asStateFlow()

    private val _opponentFleet: SnapshotStateMap<TypeOfShip, ShipState> =
        TypeOfShip.values().map { ship -> ship to ShipState.isNotSelected }.toMutableStateMap()

    val opponentFleet: Map<TypeOfShip, ShipState>
        get() = _opponentFleet

    private var _state by mutableStateOf(MatchState.IDLE)
    val state: MatchState
        get() = _state

    fun startMatch(localPlayer: PlayerInfo, challenge: Challenge): Job? =
        if (state == MatchState.IDLE) {
            _state = MatchState.STARTING
            viewModelScope.launch {
                match.start(localPlayer, challenge, _myBoard).collect {
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


    private val fleetSelectorHits = TypeOfShip.values().map { ship -> Pair(ship, 0)}.toMutableStateMap()


    fun fleetUpdater(ship: Ship?){
        if (ship != null) {
            var count = fleetSelectorHits[ship.type]
            if (count != null) {
                fleetSelectorHits[ship.type] = count + 1
            }

        if (fleetSelectorHits[ship?.type] == TypeOfShip.values().first { e -> e == ship?.type }.size)
            _opponentFleet[ship?.type!!] = ShipState.hasBeenPlaced
        }
    }

    private val _myBoard = myBoard

    fun opponentGameBoardClickHandler(line: Int, column: Int, localPlayer: PlayerInfo, challenge: Challenge): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.opponentBoardShot(Coordinate(line,column), localPlayer, challenge)
            }
        }
        else null

    fun saveGame() {

    }
}