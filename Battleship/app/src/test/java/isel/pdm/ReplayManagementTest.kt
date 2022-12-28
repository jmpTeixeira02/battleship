package isel.pdm

import isel.pdm.data.game.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class ReplayManagementTest {
    @Test
    fun `get successful dump of replay`() {
        try {
            val rep = Replay("#55", "2025-03-05", "Oponente3", listOf(TurnManager.fromString("E(0,0)"), TurnManager.fromString("P(0,0)"), TurnManager.fromString("E(1,1)"), TurnManager.fromString("P(1,1)"), TurnManager.fromString("E(2,2)"), TurnManager.fromString("P(2,2)")))

            ReplayManager.dump(System.getenv("USERPROFILE")!! + "\\Desktop\\Dump.rep", rep)
        }
        catch (ignored: Exception) {
            fail("Dump was not successful")
        }
    }

    @Test
    fun `successful sequential dump and read`() {
        val json = Json { prettyPrint = true }
        val rep = Replay("ID000001", "2019-02-28", "JoseMariaVenancioDasCouves", listOf(Turn(TurnUser.Enemy, Coordinate(0, 0)), Turn(TurnUser.Player, Coordinate(0, 0)), Turn(TurnUser.Enemy, Coordinate(1, 1)), Turn(TurnUser.Player, Coordinate(1, 1)), Turn(TurnUser.Enemy, Coordinate(2, 2)), Turn(TurnUser.Player, Coordinate(2, 2))))
        val path = System.getenv("USERPROFILE")!! + "\\Desktop\\DumpAndRead.rep"

        ReplayManager.dump(path, rep)

        val file = File(path)
        var jsonSaved = ""
        val lineNo = file.readLines().size

        for (i in 0 until lineNo) {
            jsonSaved += file.readLines()[i] + if (i != lineNo - 1) "\n" else ""
        }

        assertEquals(jsonSaved, json.encodeToString(rep))

        val repDec = ReplayManager.read(path)
        assertTrue(ReplayManager.equals(rep, repDec))
    }
}