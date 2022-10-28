package isel.pdm.service

import android.util.Log
import isel.pdm.data.players.PlayerMatchmaking
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

private val TAG = "Matchmaking"

interface Matchmaking {
    suspend fun findPlayer(): MutableList<PlayerMatchmaking>
    suspend fun sendInviteTo(playerName: String): PlayerMatchmaking
}

class FakeMatchmaking : Matchmaking{
    private val fakePlayers = mutableListOf<PlayerMatchmaking>()
    override suspend fun findPlayer(): MutableList<PlayerMatchmaking> {
        delay(2000)
        if (fakePlayers.size == 0){
            fakePlayers.add(PlayerMatchmaking("1"))
        }
        else{
            fakePlayers.add(PlayerMatchmaking((fakePlayers.last().name.toInt() + 1).toString() ))
        }
        Log.v(TAG, fakePlayers.toString())
        return fakePlayers
    }

    override suspend fun sendInviteTo(playerName: String): PlayerMatchmaking {
        return fakePlayers[fakePlayers.indexOf(PlayerMatchmaking(playerName))]
    }

}

/*
private val fakePlayers = mutableListOf<PlayerMatchmaking>(
    PlayerMatchmaking("A"),
    PlayerMatchmaking("B"),
    PlayerMatchmaking("C")
)*/
