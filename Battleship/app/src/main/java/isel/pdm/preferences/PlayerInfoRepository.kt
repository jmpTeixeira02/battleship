package isel.pdm.preferences

import android.content.Context
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.lobby.model.PlayerMatchmaking

interface PlayerRepository {

    /**
     * The user information, if already stored, or null otherwise. Accesses to
     * this property CAN be made on the main thread (a.k.a. UI thread)
     */
    var playerInfo: PlayerInfo?
}

/**
 * A user information repository implementation supported in shared preferences
 */
class PlayerInfoRepositorySharedPrefs(private val context: Context): PlayerRepository {

    private val userNameKey = "User"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var playerInfo: PlayerInfo?
        get() {
            val savedUsername = prefs.getString(userNameKey, null)
            return if (savedUsername != null)
                PlayerInfo(savedUsername)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userNameKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userNameKey, value.username)
                    .apply()
        }
}