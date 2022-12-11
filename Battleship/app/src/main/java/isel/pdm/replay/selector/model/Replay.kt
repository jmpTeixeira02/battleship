package isel.pdm.replay.selector.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Replay(
    val replayId: String,
    val date: String,
    val opponentName: String,
    val shotsFired: Int
) : Parcelable