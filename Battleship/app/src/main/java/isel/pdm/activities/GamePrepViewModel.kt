package isel.pdm.activities

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.data.game.Board
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePrepViewModel() : ViewModel() {

    private var _isSelectedList = mutableStateListOf(false, false, false, false ,false)
    val isSelectedList = _isSelectedList

    private var _isDeleting by mutableStateOf(false)
    val isDeleting: Boolean
        get() = _isDeleting

    fun updateSelectedList(idx: Int){
        if (!_isSelectedList[idx]){
            _isSelectedList.replaceAll { _ -> false }
            _isSelectedList[idx] = !_isSelectedList[idx]
        }
        else{
            _isSelectedList.replaceAll { _ -> false }
        }
    }


    private var board = Board()

    fun deleteBoat() {
        viewModelScope.launch {
            _isDeleting = true
            board.deleteBoat()
            _isDeleting = false
        }
    }
}