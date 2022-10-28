package isel.pdm.service

import isel.pdm.data.replays.Replay
import kotlinx.coroutines.delay

interface Replays {
    fun showReplays(): List<Replay>
}

class FakeReplayService : Replays {

    private val fakeReplays = mutableListOf<Replay>()

    override fun showReplays(): List<Replay> {
        //delay(1000)
        var i = 1
        while (i <= 5) {
            fakeReplays.add(Replay("#0$i", "2$i/0$i/2022"))
            ++i
        }
        return fakeReplays
    }

}