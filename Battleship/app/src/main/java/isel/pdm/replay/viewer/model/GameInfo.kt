package isel.pdm.replay.viewer.model

import android.os.Parcelable
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.Coordinate
import kotlinx.parcelize.Parcelize

@Parcelize
@kotlinx.serialization.Serializable
data class GameInfo(
    val myBoard: GameBoard,
    val opponentBoard: GameBoard,
    val myMoves: List<Coordinate>,
    val opponentMoves: List<Coordinate>,
    val iMadeFirstMove: Boolean
) : Parcelable