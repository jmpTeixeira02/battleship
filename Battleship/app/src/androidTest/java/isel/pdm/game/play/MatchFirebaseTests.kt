package isel.pdm.game.play

import isel.pdm.game.CleanupMatchesRule
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.play.model.GameEvent
import isel.pdm.game.play.model.GameStarted
import isel.pdm.localTestPlayer
import isel.pdm.testutils.SuspendingGate
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MatchFirebaseTests {

    @get:Rule
    val matchesRule = CleanupMatchesRule()

    @Test
    fun start_reports_new_game_on_returned_flow(): Unit = runTest {

        val sut = matchesRule.match
        val gameStateChangedGate = SuspendingGate()
        var gameEvent: GameEvent? = null

        // Act
        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge,
                localGameBoard = GameBoard()
            ).collect {
                gameEvent = it
                gameStateChangedGate.open()
            }
        }

        gameStateChangedGate.await()
        collectJob.cancel()

        // Assert
        assertTrue(gameEvent is GameStarted)
        assertNotNull(gameEvent)
    }
}