package isel.pdm.activities

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.InviteState
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.service.MatchmakingService
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val matchmaking: MatchmakingService) : ViewModel() {

    private var _players by mutableStateOf<List<PlayerMatchmaking>>(emptyList())
    //private var _players = mutableStateListOf<PlayerMatchmaking>()


    val players: List<PlayerMatchmaking>
        get() = _players

    private var _isRefreshing by mutableStateOf(false)
    val isRefreshing: Boolean
        get() = _isRefreshing


    fun findPlayer() {
        viewModelScope.launch {
            _isRefreshing = true
            _players = _players + matchmaking.findPlayer()
            _isRefreshing = false
        }
    }

    fun updatePlayerState(player: PlayerMatchmaking, state: InviteState){
        // Cant update property of item in list?

            /*var listPlayer: PlayerMatchmaking? =
                _players.find { listPlayer -> listPlayer == player }

            if (listPlayer != null) {
                listPlayer.inviteState = state
            }*/

        val idx = _players.indexOf(player)

        _players = _players.mapIndexed { index, playerMatchmaking ->
            if (index == idx) playerMatchmaking.copy(inviteState = state)
            else playerMatchmaking
        }
    }

    fun removePlayer(player: PlayerMatchmaking){
        _players = _players - player
    }
}
