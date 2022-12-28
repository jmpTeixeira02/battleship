package isel.pdm.testutils

import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.BattleshipTestApplication
import isel.pdm.game.lobby.model.LOBBY
import isel.pdm.game.lobby.model.Lobby
import isel.pdm.game.lobby.model.LobbyFirebase
import isel.pdm.game.lobby.model.toDocumentContent
import isel.pdm.otherTestPlayersInLobby
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PopulatedFirebaseLobby : TestRule {

    val app: BattleshipTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as BattleshipTestApplication
    }

    val lobby: Lobby = LobbyFirebase(app.emulatedFirestoreDb)

    private suspend fun populateLobby() {
        val results = otherTestPlayersInLobby.map {
            app.emulatedFirestoreDb
                .collection(LOBBY)
                .document(it.id.toString())
                .set(it.toDocumentContent())
        }
        results.forEach { it.await() }
    }

    private suspend fun emptyLobby() {
        val results = otherTestPlayersInLobby.map {
            app.emulatedFirestoreDb
                .collection(LOBBY)
                .document(it.id.toString())
                .delete()
        }
        results.forEach { it.await() }
    }


    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    populateLobby()
                    test.evaluate()
                }
                finally {
                    emptyLobby()
                }
            }
        }
}
