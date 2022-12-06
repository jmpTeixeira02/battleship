package isel.pdm.activities

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import isel.pdm.ui.elements.BOARD_SIDE
import isel.pdm.ui.elements.BoardCell
import isel.pdm.ui.elements.BoatSelector
import isel.pdm.ui.elements.FleetSelector

class GamePrepViewModel() : ViewModel() {

    private var _boardCells: SnapshotStateList<SnapshotStateList<BoardCell>> = List(BOARD_SIDE){_ -> List(BOARD_SIDE){_-> BoardCell.Water}.toMutableStateList()}.toMutableStateList()
    val boardCell = _boardCells

    fun updateCell(x: Int, y:Int, boatType: BoardCell){
        if (_isDeleting){
            if (_boardCells[y][x] == boatType) _boardCells[y][x] = BoardCell.Water
        }
        else{
            if (_boardCells[y][x] == BoardCell.Water)
                _boardCells[y][x] = boatType
        }
    }

    private var _selectedBoat: SnapshotStateList<BoatSelector> = List(5){_ -> BoatSelector.isNotSelected}.toMutableStateList()
    val selectedBoat = _selectedBoat

//    fun updateSelectedBoat(idx: Int){
//        if (!_selectedBoat[idx]){
//            _selectedBoat.replaceAll { _ -> false }
//            _selectedBoat[idx] = !_selectedBoat[idx]
//        }
//        else{
//            _selectedBoat.replaceAll { _ -> false }
//        }
//    }

    fun updateSelectedBoat(idx: Int) {
        if (_selectedBoat[idx] == BoatSelector.isNotSelected){
            val placedBoats = _selectedBoat.mapIndexed{innerIdx, state -> Pair(innerIdx, state) }.filter{pair -> pair.second == BoatSelector.hasBeenPlaced}
            _selectedBoat.replaceAll{_ -> BoatSelector.isNotSelected}
            placedBoats.forEach {
                _selectedBoat[it.first] = it.second
            }
            _selectedBoat[idx] = BoatSelector.isSelected
        }
        else if (_selectedBoat[idx] == BoatSelector.isSelected){
            _selectedBoat[idx] = BoatSelector.isNotSelected
        }
    }

    private var _isDeleting by mutableStateOf(false)
    val isDeleting: Boolean
        get() = _isDeleting

    fun deleteBoat() {
        _isDeleting = !_isDeleting
    }
}