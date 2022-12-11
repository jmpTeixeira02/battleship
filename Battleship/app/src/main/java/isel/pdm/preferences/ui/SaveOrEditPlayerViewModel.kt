package isel.pdm.preferences.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import isel.pdm.game.lobby.model.PlayerMatchmaking

class SaveOrEditPlayerViewModel : ViewModel() {
    private var _players by mutableStateOf<List<PlayerMatchmaking>>(emptyList())
    private val players: List<PlayerMatchmaking>
        get() = _players


    fun getRegisteredPlayers(): List<PlayerMatchmaking> {
        return players
    }

    fun createPlayer(player: PlayerMatchmaking) {
        //if (checkEqualPlayerName(player.username)!!.equals(null)) {
        _players = _players + player
        //  return true
        // }
        // return false
    }


    /*private fun checkEqualPlayerName(playerName: String): PlayerMatchmaking? {
        val ret =  _players.find { player ->
            player.username == playerName
        }
        Log.v()
        return ret
    }*/
}