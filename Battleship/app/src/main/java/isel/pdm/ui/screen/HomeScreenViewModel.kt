package isel.pdm.ui.screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.players.PlayerMatchmaking
import isel.pdm.service.Matchmaking
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val matchmaking: Matchmaking) : ViewModel() {

    private var _players by mutableStateOf<List<PlayerMatchmaking>>(emptyList())
    val players: List<PlayerMatchmaking>
        get() = _players

    private var _isRefreshing by mutableStateOf(false)
    val isRefreshing: Boolean
        get() = _isRefreshing


    fun findPlayer() {
        viewModelScope.launch {
            _isRefreshing = true
            _players = matchmaking.findPlayer()
            _isRefreshing = false
        }
    }


    /* private var _isInviteSent by mutableStateOf(false)
      val isInviteSent: Boolean
          get() = _isInviteSent

      private var _player by mutableStateOf<PlayerMatchmaking?>(null)
      val player: PlayerMatchmaking?
          get() = _player*/

    /*fun sendInvite() {
        viewModelScope.launch {
            _player = matchmaking.sendInviteTo(player!!.name)
        }
    }*/
}