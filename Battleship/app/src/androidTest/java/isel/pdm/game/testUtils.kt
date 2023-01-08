package isel.pdm.game

import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.BattleshipTestApplication
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.play.model.Match
import isel.pdm.game.play.model.MatchFirebase
import isel.pdm.game.play.model.ONGOING
import isel.pdm.localTestPlayer
import isel.pdm.otherTestPlayersInLobby
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CleanupMatchesRule : TestRule {

    val app: BattleshipTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as BattleshipTestApplication
    }

    val remoteInitiatedChallenge = Challenge(
        challenger = otherTestPlayersInLobby.first(),
        challenged = localTestPlayer
    )

    val locallyInitiatedChallenge = Challenge(
        challenger = localTestPlayer,
        challenged = otherTestPlayersInLobby.first(),
    )

    val match: Match = MatchFirebase(app.emulatedFirestoreDb)

    private suspend fun cleanup() {
        app.emulatedFirestoreDb
            .collection(ONGOING)
            .get()
            .await()
            .map { it.id }
            .forEach {
                app.emulatedFirestoreDb
                    .collection(ONGOING)
                    .document(it)
                    .delete()
                    .await()
            }
    }

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    test.evaluate()
                }
                finally {
                    cleanup()
                }
            }
        }
}
