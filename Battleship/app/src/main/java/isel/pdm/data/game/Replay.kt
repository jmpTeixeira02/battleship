package isel.pdm.data.game

data class Replay(val replayId: String, val date: String, val opponentName: String, val turns: MutableList<Turn>) {
    companion object {
        private var dump: String = ""

        fun dump(rep: Replay) {
            try {
                if (rep.replayId.contains("|||")) throw Exception("replay ID had illegal character sequence")
                dump = rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------"
                rep.turns.forEach {
                    dump += "\n" + it
                }
            }
            catch (e: Exception) {
                throw Exception("Could not mark game as favorite - ${e.message}")
            }
        }

        fun read() : Replay {
            try {
                val id = dump.substring(0, dump.indexOf("|||"))
                val date = dump.substring(dump.indexOf("|||") + 3, dump.lastIndexOf("|||"))
                val opponent = dump.substring(dump.lastIndexOf("|||") + 3, dump.IndexOf("\n--------------------\n")
                val turns = mutableListOf<Turn>()
                dump.substring(dump.IndexOf("\n--------------------\n" + 22), dump.length + 1).split(Regex.fromLiteral("\n")). forEach {
                    turns.add(Turn.fromString(it))
                }
                return Replay(id, date, opponent, turns)
            }
            catch (e: Exception) {
                throw Exception("Could not replay favorite game - ${e.message}")
            }
        }
    }
}

