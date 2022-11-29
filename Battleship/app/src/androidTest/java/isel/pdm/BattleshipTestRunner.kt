package isel.pdm

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import io.mockk.every
import io.mockk.mockk
import isel.pdm.data.player.PlayerMatchmaking
import isel.pdm.data.player.PlayerRepository

class BattleshipTestApplication : DependenciesContainer, Application() {
    override var playerRepo: PlayerRepository =
        mockk {
            every { playerInfo } returns PlayerMatchmaking("newUsername")
        }
}

@Suppress("unused")
class BattleshipTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}