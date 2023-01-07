package isel.pdm.replay.selector.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.model.ReplayService

class SelectReplayViewModel(private val replayService: ReplayService) : ViewModel() {

    private val _replays = replayService.fetchReplays()
    val replays: List<Replay>
        get() = _replays

}