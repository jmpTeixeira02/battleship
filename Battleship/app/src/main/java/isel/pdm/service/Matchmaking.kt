package isel.pdm.service

import android.util.Log
import isel.pdm.data.players.PlayerMatchmaking
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

private val TAG = "Matchmaking"

interface Matchmaking {
    suspend fun findPlayer(): List<PlayerMatchmaking>
    //suspend fun sendInviteTo(playerName: String): PlayerMatchmaking
}

class FakeMatchmaking : Matchmaking{
    var count: Int = 1
    override suspend fun findPlayer(): List<PlayerMatchmaking> {
        delay(2000)
        val fakePlayers = listOf(PlayerMatchmaking(count.toString()))
        count++;
        return fakePlayers
    }

    /*override suspend fun sendInviteTo(playerName: String): PlayerMatchmaking {
        return fakePlayers[fakePlayers.indexOf(PlayerMatchmaking(playerName))]
    }*/

}

/*
private val fakePlayers = mutableListOf<PlayerMatchmaking>(
    PlayerMatchmaking("A"),
    PlayerMatchmaking("B"),
    PlayerMatchmaking("C")
)*/
