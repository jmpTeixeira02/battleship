package isel.pdm.data.player

import android.content.Context

interface PlayerRepository {

    /**
     * The user information, if already stored, or null otherwise. Accesses to
     * this property CAN be made on the main thread (a.k.a. UI thread)
     */
    var playerInfo: PlayerMatchmaking?
}

/**
 * A user information repository implementation supported in shared preferences
 */
class PlayerInfoRepositorySharedPrefs(private val context: Context): PlayerRepository {

    private val userNameKey = "User"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var playerInfo: PlayerMatchmaking?
        get() {
            val savedUsername = prefs.getString(userNameKey, null)
            return if (savedUsername != null)
                PlayerMatchmaking(savedUsername)
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