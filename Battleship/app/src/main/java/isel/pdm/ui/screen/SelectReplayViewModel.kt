package isel.pdm.ui.screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import isel.pdm.data.replays.Replay
import isel.pdm.service.Replays

class SelectReplayViewModel(private val replay: Replays) : ViewModel() {

    private var _replays by mutableStateOf<List<Replay>>(emptyList())
    val replays: List<Replay>
        get() = _replays


    fun getAvailableReplays(): List<Replay> {
        return replay.showReplays()
    }

}