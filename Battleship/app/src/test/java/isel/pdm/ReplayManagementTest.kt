package isel.pdm

import isel.pdm.data.game.*
import org.junit.Assert.*
import org.junit.Test

class ReplayManagementTest {
    @Test
    fun `get replay from string`() {
        val rep = ReplayManager.read("ID000001|||2019-02-28|||JoseMariaVencancioDasCouves\n" +
                "--------------------\n" +
                "E(0,0)\n" +
                "P(0,0)\n" +
                "E(1,1)\n" +
                "P(1,1)\n" +
                "E(2,2)\n" +
                "P(2,2)")

        assertEquals("ID000001", rep.replayId)
        assertEquals("2019-02-28", rep.date)
        assertEquals("JoseMariaVencancioDasCouves", rep.opponentName)

        val expectedTurns = listOf(TurnManager.fromString("E(0,0)"), TurnManager.fromString("P(0,0)"), TurnManager.fromString("E(1,1)"), TurnManager.fromString("P(1,1)"), TurnManager.fromString("E(2,2)"), TurnManager.fromString("P(2,2)"))
        assertEquals(expectedTurns, rep.turns)
    }

    @Test
    fun `get successful dump of replay`() {
        val rep = Replay("ID000001", "2019-02-28", "JoseMariaVencancioDasCouves", listOf(TurnManager.fromString("E(0,0)"), TurnManager.fromString("P(0,0)"), TurnManager.fromString("E(1,1)"), TurnManager.fromString("P(1,1)"), TurnManager.fromString("E(2,2)"), TurnManager.fromString("P(2,2)")))

        val expected = "ID000001|||2019-02-28|||JoseMariaVencancioDasCouves\n" +
                "--------------------\n" +
                "E(0,0)\n" +
                "P(0,0)\n" +
                "E(1,1)\n" +
                "P(1,1)\n" +
                "E(2,2)\n" +
                "P(2,2)"

        assertEquals(expected, ReplayManager.dump(rep))
    }

    @Test
    fun `successful sequential dump and read`() {
        val toDump = "ID000001|||2019-02-28|||JoseMariaVencancioDasCouves\n" +
                "--------------------\n" +
                "E(0,0)\n" +
                "P(0,0)\n" +
                "E(1,1)\n" +
                "P(1,1)\n" +
                "E(2,2)\n" +
                "P(2,2)"

        val dump = ReplayManager.read(toDump)

        assertEquals("ID000001", dump.replayId)
        assertEquals("2019-02-28", dump.date)
        assertEquals("JoseMariaVencancioDasCouves", dump.opponentName)

        val expectedTurns = listOf(TurnManager.fromString("E(0,0)"), TurnManager.fromString("P(0,0)"), TurnManager.fromString("E(1,1)"), TurnManager.fromString("P(1,1)"), TurnManager.fromString("E(2,2)"), TurnManager.fromString("P(2,2)"))
        assertEquals(expectedTurns, dump.turns)

        val readAfterDump = ReplayManager.dump(dump)

        assertEquals(toDump, readAfterDump)
    }
}