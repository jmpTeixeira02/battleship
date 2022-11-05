package isel.pdm.activities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.game.Board
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePrepViewModel() : ViewModel() {

    private var _isDeleting by mutableStateOf(false)
    val isDeleting: Boolean
        get() = _isDeleting

    private var board = Board()

    fun deleteBoat() {
        viewModelScope.launch {
            _isDeleting = true
            board.deleteBoat()
            _isDeleting = false
        }
    }
}