package isel.pdm.replay.selector.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.model.ReplayService

class SelectReplayViewModel(private val replay: ReplayService) : ViewModel() {

    private var _replays by mutableStateOf<List<Replay>>(emptyList())
    private val replays: List<Replay>
        get() = _replays


    fun getAvailableReplays(): List<Replay> {
        return replays
    }

    fun getNewReplays() {
        if (_replays.isEmpty())
            _replays = _replays + replay.fetchReplays()
    }

    fun openReplay(replay: Replay): Replay {
        val idx = _replays.indexOf(replay)
        return _replays[idx]
    }

}