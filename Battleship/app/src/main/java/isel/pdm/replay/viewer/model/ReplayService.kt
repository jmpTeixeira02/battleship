package isel.pdm.replay.viewer.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.selector.model.ReplayManager

interface ReplayService {
    fun fetchReplays(): List<Replay>
}

class RealReplayService(val context: Context) : ReplayService{

    override fun fetchReplays(): List<Replay> {
        return ReplayManager.loadAllReplays(context = context)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
class FakeReplayService : ReplayService {

    private val fakeReplays = mutableListOf<Replay>()


    override fun fetchReplays(): List<Replay> {
        var i = 1
        while (i <= 5) {
            fakeReplays.add(Replay(opponentName = "$i", shotsFired = 1))
            ++i
        }
        return fakeReplays
    }

}