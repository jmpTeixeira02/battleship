package isel.pdm.service

import isel.pdm.data.Replay

interface ReplayService {
    fun fetchReplays(): List<Replay>
}

class FakeReplayService : ReplayService {

    private val fakeReplays = mutableListOf<Replay>()

    override fun fetchReplays(): List<Replay> {
        var i = 1
        while (i <= 5) {
            fakeReplays.add(Replay("#0$i", "2$i/0$i/2022", "hehexd", 10))
            ++i
        }
        return fakeReplays
    }

}