package isel.pdm.replay.viewer.model

import android.os.Build
import androidx.annotation.RequiresApi
import isel.pdm.replay.selector.model.Replay

interface ReplayService {
    fun fetchReplays(): List<Replay>
}

class FakeReplayService : ReplayService {

    private val fakeReplays = mutableListOf<Replay>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun fetchReplays(): List<Replay> {
        var i = 1
        while (i <= 5) {
            fakeReplays.add(Replay(opponentName = "$i", shotsFired = 1))
            ++i
        }
        return fakeReplays
    }

}