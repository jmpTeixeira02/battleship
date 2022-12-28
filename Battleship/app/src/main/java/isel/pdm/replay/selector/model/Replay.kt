package isel.pdm.replay.selector.model

import android.os.Parcelable
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.Coordinate
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Parcelize
@Serializable
data class GameInfo(
    val myBoard: GameBoard,
    val opponentBoard: GameBoard,
    val myMoves: List<Coordinate>,
    val opponentMoves: List<Coordinate>,
    val iMadeFirstMove: Boolean
) : Parcelable

@Parcelize
@Serializable
data class Replay(
    val replayId: String,
    val date: String,
    val opponentName: String,
    val shotsFired: Int,
    val gameInfo: GameInfo = GameInfo(GameBoard(), GameBoard(), listOf(), listOf(), true)
) : Parcelable

class ReplayManager {
    companion object {
        private fun leapYear(year: Int): Boolean {
            return year % 400 == 0 ||
                    (year % 100 != 0 && year % 4 == 0)
        }

        private fun checkDate(date: String): Boolean {
            val members = date.split(Regex.fromLiteral("-"))
            if (members.size != 3) return false

            val numbersList = listOf(Integer.parseInt(members[0]), Integer.parseInt(members[1]), Integer.parseInt(members[2]))

            return when (numbersList[1]) {
                2 -> numbersList[2] in 1..if (leapYear(numbersList[0])) 28 else 29
                4, 6, 9, 11 -> numbersList[2] in 1..30
                1, 3, 5, 7, 8, 10, 12 -> numbersList[2] in 1..31
                else -> false
            }
        }

        fun dump(path: String, rep: Replay) {
            try {
                if (!File(path).isDirectory) throw Exception("given path is not a directory")
                if (!checkDate(rep.date)) throw Exception("invalid date in replay properties")

                val json = Json { prettyPrint = true }

                val filepath = path + "/" + rep.replayId + ".rep"

                val replay = json.encodeToString(rep)

                val file = File(filepath)
                file.writeText(replay)
            }
            catch (e: Exception) {
                println("Could not mark game as favorite - ${e.message}")
            }
        }

        fun read(path: String) : Replay {
            try {
                val repFile = File(path)
                val content = repFile.readText()

                return Json.decodeFromString(content)
            }
            catch (e: Exception) {
                throw Exception("Could not replay favorite game: ${e.message}")
            }
        }

        fun equals(first: Replay, second: Replay): Boolean {
            /* If these properties are the same, there is a high
               chance that the game info is the same*/
            return first == second ||
                   (first.replayId == second.replayId &&
                    first.date == second.date &&
                    first.opponentName == second.opponentName &&
                    first.shotsFired == second.shotsFired)
        }
    }
}