package isel.pdm.replay.selector.model

import android.content.Context
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import isel.pdm.game.play.model.GameBoard
import isel.pdm.replay.viewer.model.GameInfo
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
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
    val gameInfo: GameInfo = GameInfo(GameBoard(), GameBoard(), listOf(), listOf(), true),
    val winner: String = ""
) : Parcelable


class ReplayManager{
    companion object{
        private fun encoder(replay: Replay): String{
            return Json.encodeToString(Replay.serializer(), replay)
        }

        @OptIn(ExperimentalSerializationApi::class)
        private fun decoder(encodedFile: InputStream): Replay{
            return Json.decodeFromStream(encodedFile)
        }

        fun saveReplay(context: Context, replay: Replay){
            context.openFileOutput(replay.replayId, Context.MODE_PRIVATE).use {
                it.write( encoder(replay).toByteArray() )
            }
        }

        fun loadReplay(context: Context, filename: String): Replay{
            return decoder(context.openFileInput(filename))
        }

        fun loadAllReplays(context: Context): List<Replay>{
            val files = context.filesDir.listFiles()
            return files?.filter { it.canRead() && it.isFile }?.map {
                decoder(it.inputStream())
            } ?: listOf()
        }
    }
}