package isel.pdm.data.game

import android.os.Parcelable
import java.io.File
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Parcelize
@Serializable
data class Replay(val replayId: String = "", val date: String = "0000-01-01", val opponentName: String, val turns: List<Turn> = listOf()) :
    Parcelable

class ReplayManager {
    companion object {
        private fun leapYear(year: Int): Boolean {
            return year % 400 == 0 ||
                   (year % 100 != 0 && year % 4 == 0)
        }

        private fun checkDate(date: String): Boolean {
            val members = date.split(Regex.fromLiteral("-"))
            if (members.size != 3) return false;

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
                if (!checkDate(rep.date)) throw Exception("invalid date in replay properties")

                val json = Json { prettyPrint = true }

                var filepath = path
                if (!path.endsWith(".rep")) filepath += ".rep"

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
            if (first == second) return true
            var turnsAreSame = true

            if (first.turns.size != second.turns.size)
                turnsAreSame = false
            else {
                for (i in 0 until first.turns.size) {
                    if (first.turns[i].user != second.turns[i].user &&
                        first.turns[i].coords.column != second.turns[i].coords.column &&
                        first.turns[i].coords.line != second.turns[i].coords.line) {
                        turnsAreSame = false
                        break
                    }
                }
            }

            return first == second ||
                   (first.replayId == second.replayId &&
                    first.date == second.date &&
                    first.opponentName == second.opponentName &&
                    turnsAreSame)
        }
    }
}