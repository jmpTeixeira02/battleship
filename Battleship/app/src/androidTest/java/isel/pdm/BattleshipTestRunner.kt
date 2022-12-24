package isel.pdm

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import androidx.test.runner.screenshot.Screenshot.capture
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import isel.pdm.game.lobby.model.*
import isel.pdm.preferences.PlayerRepository
import kotlinx.coroutines.flow.flow


val localTestPlayer = PlayerInfo("local")

val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3 ) {
        add(PlayerInfo("remote $it"))
    }
}

class BattleshipTestApplication : DependenciesContainer, Application() {
    override var playerRepo: PlayerRepository =
        mockk {
            every { playerInfo } returns localTestPlayer
        }

    override var lobby: Lobby =
        mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            every { enterAndObserve(capture(localPlayer)) } returns flow {
                emit(RosterUpdated(
                    buildList {
                        add(localPlayer.captured)
                        addAll(otherTestPlayersInLobby)
                    }
                ))
            }

            coEvery { enter(capture(localPlayer)) } answers {
                buildList {
                    add(localPlayer.captured)
                    addAll(otherTestPlayersInLobby)
                }
            }

            val opponent = slot<PlayerInfo>()
            coEvery { issueChallenge(capture(opponent)) } answers {
                Challenge(
                    challenger = localPlayer.captured,
                    challenged = opponent.captured
                )
            }

            coEvery { leave() } returns Unit
        }
}

@Suppress("unused")
class BattleshipTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}