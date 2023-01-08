package isel.pdm

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import isel.pdm.game.lobby.model.*
import isel.pdm.game.play.model.*
import isel.pdm.game.prep.model.Board
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


    override var match: Match = mockk(relaxed = true) {
        val localPlayer = slot<PlayerInfo>()
        val challenge = slot<Challenge>()
        val localGameBoard = slot<GameBoard>()
        coEvery { start(capture(localPlayer), capture(challenge), capture(localGameBoard)) } answers {
            flow {
                val localMarker = getLocalPlayerMarker(localPlayer.captured, challenge.captured)
                emit(GameStarted(Game(localPlayerMarker = localMarker, challengerBoard = GameBoard())))
            }
        }
    }

    val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }
}

@Suppress("unused")
class BattleshipTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}