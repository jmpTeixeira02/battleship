package isel.pdm.service

import android.util.Log
import isel.pdm.data.players.InviteState
import isel.pdm.data.players.PlayerMatchmaking
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

private val TAG = "Matchmaking"

interface Matchmaking {
    suspend fun findPlayer(): List<PlayerMatchmaking>
}

class FakeMatchmaking : Matchmaking{
    var count: Int = 1
    override suspend fun findPlayer(): List<PlayerMatchmaking> {
        delay(500)
        val fakePlayers = listOf(PlayerMatchmaking(count.toString()), PlayerMatchmaking((count + 1).toString(), inviteState = InviteState.InvitePending))
        count += 2
        return fakePlayers
    }

}