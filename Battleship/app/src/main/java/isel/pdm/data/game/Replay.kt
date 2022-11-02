package isel.pdm.data.game

data class Replay(val replayId: String, val date: String, val opponentName: String, val turns: MutableList<Turn>) {
    companion object {
        var dump: String = ""

        public fun Dump(rep: Replay) : Unit {
            try {
                if (rep.replayId.contains("|||")) throw Exception("replay ID had illegal character sequence")
                dump = rep.replayId + "|||" + rep.date + "|||" + rep.opponentName + "\n--------------------"
                rep.turns.forEach {
                    dump += it
                }
            }
            catch (e: Exception) {
                throw Exception("Could not mark game as favorite - ${e.message}")
            }
        }

        public fun Read() : Replay {
            try {
                
            }
            catch (e: Exception) {
                throw Exception("Could not replay favorite game - ${e.message}")
            }
        }
    }
}

