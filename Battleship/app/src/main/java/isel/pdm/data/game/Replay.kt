package isel.pdm.data.game

import android.os.Parcelable
import java.io.File
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

// TODO - finish correct date implementation, create dedicated data structure and create unit tests

@Parcelize
data class Replay(val replayId: String = "", val date: String = /*LocalDate.now().toString()*/"", val opponentName: String, val turns: List<Turn> = listOf()) :
    Parcelable

class ReplayManager {
    companion object {

        fun dump(/*path: String, */rep: Replay): String {
            try {
                var data = ""
                if (rep.replayId.contains("|||")) throw Exception("replay ID had illegal character sequence")

                // This line tests if the given date is valid; if it is not valid, a DateTimeException
                // is thrown. Since the main objective is date testing, the return variable can be
                // discarded, since it will not be used
                //LocalDate.parse(rep.date)

                data = rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------"
                rep.turns.forEach {
                    data += "\n" + TurnManager.toString(it)
                }

                /*
                var file = File(path)
                file.writeText(rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------")
                rep.turns.forEach {
                    file.appendText("\n" + it)
                }
                 */

                return data
            }
            catch (e: Exception) {
                throw Exception("Could not mark game as favorite - ${e.message}")
            }
        }

        fun read(/*path*/data: String) : Replay {
            try {
                val header = readHeaderInfo(data)
                val turns = mutableListOf<Turn>()
                val turnList = data.substring(data.indexOf("\n--------------------\n") + 22, data.length)
                turnList.split(Regex.fromLiteral("\n")).forEach {
                    turns.add(TurnManager.fromString(it))
                }

                /*
                val file = File(path).readLines()
                val header = file.subList(0, 1).split("|||")
                val turns = file.subList(2, file.size + 1)
                 */

                return Replay(header[0], header[1], header[2], turns)
            }
            catch (e: Exception) {
                throw Exception("Could not replay favorite game: ${e.message}")
            }
        }

        fun readHeaderInfo(/*path*/data: String): List<String> {
            try {
                return data.substring(0, data.indexOf("\n")).split("|||")

                /*
                val header = File(path).readLines()[0]
                return header.split(Regex.fromLiteral("|||"))
                 */
            }
            catch (e: Exception) {
                throw Exception("Could not retrieve replay info - ${e.message}")
            }

        }

        fun getTurns(path: String): List<Turn> {
            val file = File(path)
            val temp: List<String> = file.readLines()
            val res = mutableListOf<Turn>()
            temp.subList(2, temp.size + 1).forEach {
                res.add(TurnManager.fromString(it))
            }

            return res
        }
    }
}