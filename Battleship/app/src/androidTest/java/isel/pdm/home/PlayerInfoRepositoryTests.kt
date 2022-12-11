package isel.pdm.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.preferences.PlayerInfoRepositorySharedPrefs
import isel.pdm.game.lobby.model.PlayerMatchmaking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerInfoRepositoryTests {

    private val repo by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        PlayerInfoRepositorySharedPrefs(context)
    }

    @Test
    fun setting_to_null_clears_playerInfo() {
        // Arrange
        repo.playerInfo = PlayerMatchmaking("user", )
        Assert.assertNotNull(repo.playerInfo)

        // Act
        repo.playerInfo = null

        // Assert
        Assert.assertNull(repo.playerInfo)
    }
}