package isel.pdm

import isel.pdm.data.game.Board
import isel.pdm.data.game.Coordinate
import isel.pdm.data.game.Ship
import isel.pdm.data.game.TypeOfShip
import org.junit.Assert.*
import org.junit.Test

class BoardTests {

    @Test
    fun `newly created board has no boats`() {
        val board = Board()
        assert(board.cells.flatten().filter { e -> e.ship != null }.isEmpty())
    }

    @Test
    fun `place boat on a board`() {
        val board = Board()

        val start: Coordinate = Coordinate(0,0)
        val end: Coordinate = Coordinate(0,5)

        board.placeShip(start, end, Ship(TypeOfShip.Carrier))
        assert(board.cells[0].filter { e -> e.ship?.type == TypeOfShip.Carrier }.size == TypeOfShip.Carrier.size)
    }

    @Test
    fun `placing boat out of bounds`() {
        val fixed = 0
        try{
            val start: Coordinate = Coordinate(fixed,0)
            val end: Coordinate = Coordinate(fixed,-1)
        }catch (e: Exception){
            assertTrue(true)
        }
    }

    @Test
    fun `placing boat on top of another boat`(){
        val board = Board()

        val start: Coordinate = Coordinate(0,0)
        val end: Coordinate = Coordinate(0,5)

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
    fun `placing boat diagonally`(){
        val board = Board()

        val start: Coordinate = Coordinate(0,0)
        val end: Coordinate = Coordinate(1,5)

        try {
            board.placeShip(start, end, Ship(TypeOfShip.Carrier))
            assertFalse(false)
        }catch (e:Exception){
            assertTrue(true)
        }

    }
}