package isel.pdm.replay.selector.model

import android.content.Context
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import isel.pdm.game.play.model.Game
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.Coordinate
import isel.pdm.replay.viewer.model.GameInfo
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.time.LocalDate
import java.util.*

@Parcelize
@Serializable
data class Replay @RequiresApi(Build.VERSION_CODES.O) constructor(
    val replayId: String = UUID.randomUUID().toString(),
    val replayName: String = " ",
    val date: String = LocalDate.now().toString(),
    val opponentName: String,
    val shotsFired: Int,
    val gameInfo: GameInfo = GameInfo(GameBoard(), GameBoard(), listOf(), listOf(), true)
) : Parcelable


class ReplayManager{
    companion object{
        fun saveReplay(context: Context, replay: Replay){
            val repJson = Json.encodeToString(Replay.serializer(), replay)
            context.openFileOutput(replay.replayId, Context.MODE_PRIVATE).use {
                it.write(repJson.toByteArray())
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun readReplay(context: Context, filename: String): Replay{
            val b = context.openFileInput(filename).bufferedReader().readText()
            return Json.decodeFromStream<Replay>(context.openFileInput(filename))
        }
    }
}