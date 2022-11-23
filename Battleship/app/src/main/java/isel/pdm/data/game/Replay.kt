package isel.pdm.data.game

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.time.LocalDate

data class Replay(val replayId: String, val date: String = LocalDate.now().toString(), val opponentName: String, val turns: List<Turn> = listOf()) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        LocalDate.parse(parcel.readString()!!).toString(),
        parcel.readString()!!,
        TODO("turns")
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeInt(p1)
    }

    companion object CREATOR : Parcelable.Creator<Replay> {
        override fun createFromParcel(parcel: Parcel): Replay {
            return Replay(parcel)
        }

        override fun newArray(size: Int): Array<Replay?> {
            val temp = arrayOfNulls<Replay?>(size)
            for (i in 0 until size) {
                temp[i] = Replay("Standard ID", LocalDate.now().toString(), "Opponent Name", emptyList())
            }
            return temp
        }

        private var dump: String = ""

        fun dump(/*path: String, */rep: Replay) {
            try {
                if (rep.replayId.contains("|||")) throw Exception("replay ID had illegal character sequence")

                // This line tests if the given date is valid; if it is not valid, a DateTimeException
                // is thrown. Since the main objective is date testing, the return variable can be
                // discarded, since it will not be used
                LocalDate.parse(rep.date)

                dump = rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------"
                rep.turns.forEach {
                    dump += "\n" + it
                }

                /*
                var file = File(path)
                file.writeText(rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------")
                rep.turns.forEach {
                    file.appendText("\n" + it)
                }
                 */
            }
            catch (e: Exception) {
                throw Exception("Could not mark game as favorite - ${e.message}")
            }
        }

        fun read(/*path: String*/) : Replay {
            try {
                val header = readHeaderInfo()
                val turns = mutableListOf<Turn>()
                dump.substring(dump.indexOf("\n--------------------\n") + 22, dump.length + 1).split(Regex.fromLiteral("\n")).forEach {
                    turns.add(Turn.fromString(it))
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

        fun readHeaderInfo(/*path: String*/): List<String> {
            try {
                return dump.substring(0, dump.indexOf("\n")).split("|||")

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
            val temp = file.readLines()
            val res = emptyList<Turn>().toMutableList()
            temp.subList(2, temp.size).forEach {
                res.add(Turn.fromString(it))
            }

            return res
        }
    }
}

