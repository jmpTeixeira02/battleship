package isel.pdm.data

data class Replay(val replayId: String, val date: String, val opponentName: String, turns: MutableList<Turn>)

