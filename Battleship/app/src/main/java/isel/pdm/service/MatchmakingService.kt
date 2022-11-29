package isel.pdm.service

import isel.pdm.data.player.InviteState
import isel.pdm.data.player.PlayerMatchmaking
import kotlinx.coroutines.*

private val TAG = "Matchmaking"

interface MatchmakingService {
    suspend fun findPlayer(): List<PlayerMatchmaking>
}

class FakeMatchmakingService : MatchmakingService{
    var count: Int = 1
    override suspend fun findPlayer(): List<PlayerMatchmaking> {
       // delay(500)
        val fakePlayers = listOf(PlayerMatchmaking(count.toString()), PlayerMatchmaking((count + 1).toString(), inviteState = InviteState.InvitePending))
        count += 2
        return fakePlayers
    }

}