package isel.pdm.replay.selector.model

import android.os.Parcelable
import isel.pdm.game.play.model.Game
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.Coordinate
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameInfo(
    val myBoard: GameBoard,
    val opponentBoard: GameBoard,
    val myMoves: List<Coordinate>,
    val opponentMoves: List<Coordinate>,
    val iMadeFirstMove: Boolean
) : Parcelable

@Parcelize
data class Replay(
    val replayId: String,
    val date: String,
    val opponentName: String,
    val shotsFired: Int,
    val gameInfo: GameInfo = GameInfo(GameBoard(), GameBoard(), listOf(), listOf(), true)
) : Parcelable