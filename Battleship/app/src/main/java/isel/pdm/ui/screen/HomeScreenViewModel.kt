package isel.pdm.ui.screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.players.PlayerMatchmaking
import isel.pdm.service.Matchmaking
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val matchmaking: Matchmaking): ViewModel() {

    private var _players by mutableStateOf<List<PlayerMatchmaking>>(emptyList())
    val players: List<PlayerMatchmaking>
        get() = _players

    private var _isRefreshing by mutableStateOf(false)
    val isRefreshing: Boolean
        get() = _isRefreshing

    fun findPlayer(){
        viewModelScope.launch {
            _isRefreshing = true
            _players = matchmaking.findPlayer()
            _isRefreshing = false
        }
    }
}