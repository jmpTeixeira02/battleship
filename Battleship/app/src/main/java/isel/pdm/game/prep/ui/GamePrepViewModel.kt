package isel.pdm.game.prep.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import isel.pdm.game.prep.model.*

data class ShipPlacer(var line:Int, var column: Int, var cellTenant: Cell = Cell(null))


class GamePrepViewModel : ViewModel() {

    private val _board = Board()

    private val _boardCells = _board.cells
    val boardCells = _boardCells


    private var _shipStarter: ShipPlacer? = null

    fun boardClickHandler(line: Int, column:Int, selectedShip: Ship?){
        if (_isDeleting){ // Cell was clicked while deleting ships
            val clickedShip = _boardCells[line][column].ship
            if (clickedShip!=null){
                deleteShip(clickedShip)
            }
            return
        }

        if (_boardCells[line][column].ship != null || selectedShip == null) // There was no ship selected or there was a ship on that cell already
            return

        if (_shipStarter == null){
            _boardCells[line][column] = Cell(selectedShip)
            _shipStarter = ShipPlacer(line, column, Cell(selectedShip))
        }
        else if (_shipStarter!!.cellTenant.ship == selectedShip){
            placeShip(ShipPlacer(line, column, Cell(selectedShip)))
        }
    }

    private val _shipSelector: SnapshotStateMap<TypeOfShip, ShipState> =
        TypeOfShip.values().map { ship -> ship to ShipState.isNotSelected}.toMutableStateMap()

    val shipSelector: Map<TypeOfShip, ShipState>
        get() = _shipSelector

    fun shipSelectorHandler(shipSelected: TypeOfShip) {
        if (_shipStarter != null && _shipSelector[shipSelected] != ShipState.hasBeenPlaced){ // Remove old shipStarter if a new ship is selected
            deleteShip(_shipStarter!!.cellTenant.ship!!)
            _shipStarter = null
        }
        if (isDeleting && _shipSelector[shipSelected] != ShipState.hasBeenPlaced){ // Make it impossible to select a ship when deleting
            return
        }


        if (_shipSelector[shipSelected] == ShipState.isNotSelected){
            unselectShipSelector()
            _shipSelector[shipSelected] = ShipState.isSelected
        }
        else if (_shipSelector[shipSelected] == ShipState.isSelected){
            unselectShipSelector()
        }
        else if (_shipSelector[shipSelected] == ShipState.hasBeenPlaced && isDeleting){
            deleteShip(Ship(shipSelected))
        }
    }

    private var _isDeleting by mutableStateOf(false)
    val isDeleting: Boolean
        get() = _isDeleting

    fun deleteBoatToggle() {
        unselectShipSelector()
        if (_shipStarter != null){
            deleteShip(_shipStarter!!.cellTenant.ship!!)
            _shipStarter = null
        }
        _isDeleting = !_isDeleting
    }

    fun randomFleet(){
        _board.randomFleet()
        placeAllShipSelector()
    }
    /**
     * Deletes [ship] from board and reset it's selector button
     */
    private fun deleteShip(ship: Ship){
        _shipSelector[ship.type] = ShipState.isNotSelected
        _board.deleteShip(ship)
    }

    /**
     * Places [ship] on the board, starting on [_shipStarter] local member and ending on [shipEndCoordinate]
     * If the placement is valid, the ship is placed on board and the selector is locked
     * If the placement is invalid, restart placement from [_shipStarter]
     */
    private fun placeShip(shipEnd: ShipPlacer){
        val ship =  shipEnd.cellTenant.ship!!
        try{
            _boardCells[_shipStarter!!.line][_shipStarter!!.column] = Cell(null)
            _board.placeShip(Coordinate(_shipStarter!!.line, _shipStarter!!.column), Coordinate(shipEnd.line, shipEnd.column), ship)
            _shipSelector[ship.type] = ShipState.hasBeenPlaced
            _shipStarter = null
        }catch (e: Exception){ // Invalid ship placement, restart from shipStarter
            _boardCells[_shipStarter!!.line][_shipStarter!!.column] = Cell(ship)
        }
    }

    /**
     * Makes all ships in [_shipSelector] notSelected if they haven't been placed yet
     */
    private fun unselectShipSelector(){
        _shipSelector.forEach { (k: TypeOfShip, _) ->
            if(_shipSelector[k] != ShipState.hasBeenPlaced)
                _shipSelector[k] = ShipState.isNotSelected }
    }

    /**
     * Makes all ships in [_shipSelector] placed
     */
    private fun placeAllShipSelector() {
        _shipSelector.forEach { (k: TypeOfShip, _) ->
            _shipSelector[k] = ShipState.hasBeenPlaced
        }
    }
}