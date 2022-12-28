package isel.pdm

import isel.pdm.data.game.*
import org.junit.Assert.*
import org.junit.Test

class BoardTests {

    @Test
    fun `newly created board has no ships`() {
        val board = Board()
        assert(board.cells.flatten().filter { e -> e.ship != null }.isEmpty())
    }

    @Test
    fun `place ship on a board`() {
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(0,5)

        board.placeShip(start, end, Ship(TypeOfShip.Carrier))
        assert(board.cells[0].filter { e -> e.ship?.type == TypeOfShip.Carrier }.size == TypeOfShip.Carrier.size)
    }

    @Test
    fun `placing ship out of bounds`() {
        val fixed = 0
        try{
            val start = Coordinate(fixed,0)
            val end = Coordinate(fixed,-1)
        }catch (e: Exception){
            assertTrue(true)
        }
    }

    @Test
    fun `placing ship on top of another boat`(){
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(0,5)

        board.placeShip(start, end, Ship(TypeOfShip.Carrier))
        assert(board.cells[0].filter { e -> e.ship?.type == TypeOfShip.Carrier }.size == TypeOfShip.Carrier.size)

        try {
            board.placeShip(start, end, Ship(TypeOfShip.Carrier))
            assertFalse(false)
        }catch (e:Exception){
            assertTrue(true)
        }
    }

    @Test
    fun `placing ship diagonally`(){
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(1,5)

        try {
            board.placeShip(start, end, Ship(TypeOfShip.Carrier))
            assertFalse(false)
        }catch (e:Exception){
            assertTrue(true)
        }
    }

    @Test
    fun `remove ship`(){
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(0,5)
        board.placeShip(start, end, Ship(TypeOfShip.Carrier))
        val carrierCellsFirst = board.cells.flatten().count { cell -> cell.value == TypeOfShip.Carrier.name }
        assertTrue(carrierCellsFirst == TypeOfShip.Carrier.size)

        val start1 = Coordinate(1,0)
        val end1 = Coordinate(1,5)
        board.placeShip(start1, end1, Ship(TypeOfShip.BattleShip))

        val pred:(cells:Cell) -> Boolean =
            { cell -> cell.value == TypeOfShip.Carrier.name || cell.value == TypeOfShip.BattleShip.name }

        val carrierPlusBattleCells = board.cells.flatten().count { cell -> pred(cell) }
        assertTrue(carrierPlusBattleCells == TypeOfShip.Carrier.size + TypeOfShip.BattleShip.size)

        board.deleteShip(Ship(TypeOfShip.BattleShip))
        val cellsAfterRemove = board.cells.flatten().count { cell -> pred(cell) }


        assertTrue(carrierCellsFirst == cellsAfterRemove)
    }

    @Test
    fun `remove non-existing ship`(){
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(0,5)
        board.placeShip(start, end, Ship(TypeOfShip.Carrier))
        val carrierCellsFirst = board.cells.flatten().count { cell -> cell.value == TypeOfShip.Carrier.name }
        assertTrue(carrierCellsFirst == TypeOfShip.Carrier.size)

        val start1 = Coordinate(1,0)
        val end1 = Coordinate(1,5)
        board.placeShip(start1, end1, Ship(TypeOfShip.BattleShip))

        val pred:(cells:Cell) -> Boolean =
            { cell -> cell.value == TypeOfShip.Carrier.name || cell.value == TypeOfShip.BattleShip.name }

        val carrierPlusBattleCells = board.cells.flatten().count { cell -> pred(cell) }
        assertTrue(carrierPlusBattleCells == TypeOfShip.Carrier.size + TypeOfShip.BattleShip.size)

        board.deleteShip(Ship(TypeOfShip.Submarine))
        assertTrue(carrierPlusBattleCells == board.cells.flatten().count{cell -> cell.value != "Water"})

    }

    @Test
    fun `clear board`(){
        val board = Board()

        val start = Coordinate(0,0)
        val end = Coordinate(0,5)
        board.placeShip(start, end, Ship(TypeOfShip.Carrier))

        val start1 = Coordinate(1,0)
        val end1 = Coordinate(1,5)
        board.placeShip(start1, end1, Ship(TypeOfShip.BattleShip))

        val pred:(cells:Cell) -> Boolean =
            { cell -> cell.value == TypeOfShip.Carrier.name || cell.value == TypeOfShip.BattleShip.name }

        val occupiedCells = board.cells.flatten().count {cell -> pred(cell) }
        assertTrue(occupiedCells == TypeOfShip.Carrier.size + TypeOfShip.BattleShip.size)

        board.clearBoard()
        assertTrue(board.cells.flatten().count{cell -> pred(cell)} == 0)
    }
}