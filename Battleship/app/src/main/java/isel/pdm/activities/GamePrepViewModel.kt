package isel.pdm.activities

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.data.PlayerMatchmaking
import isel.pdm.data.game.Board
import isel.pdm.ui.elements.BOARD_SIDE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePrepViewModel() : ViewModel() {

    private var _boardCells: SnapshotStateList<SnapshotStateList<Color>> = List(BOARD_SIDE){_ -> List(BOARD_SIDE){e -> Color.LightGray}.toMutableStateList()}.toMutableStateList()
    val boardCell = _boardCells

    fun updateCell(x: Int, y:Int, value: Color){
        if (_boardCells[y][x] == Color.LightGray)
            _boardCells[y][x] = value
    }

    private var _selectedBoat = mutableStateListOf(false, false, false, false ,false)
    val selectedBoat = _selectedBoat
    
    fun updateSelectedBoat(idx: Int){
        if (!_selectedBoat[idx]){
            _selectedBoat.replaceAll { _ -> false }
            _selectedBoat[idx] = !_selectedBoat[idx]
        }
        else{
            _selectedBoat.replaceAll { _ -> false }
        }
    }

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