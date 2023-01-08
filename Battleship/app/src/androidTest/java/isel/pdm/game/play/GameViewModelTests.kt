package isel.pdm.game.play

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import isel.pdm.DependenciesContainer
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.play.model.*
import isel.pdm.game.play.ui.GameViewModel
import isel.pdm.game.play.ui.MatchState
import isel.pdm.game.prep.model.Coordinate
import isel.pdm.localTestPlayer
import isel.pdm.otherTestPlayersInLobby
import isel.pdm.testutils.SuspendingGate
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GameViewModelTests {

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as DependenciesContainer
    }

    @Test
    fun initial_match_state_is_IDLE() {
        val sut = GameViewModel(app.match, GameBoard())
        assertEquals(MatchState.IDLE, sut.state)
    }

    @Test
    fun startMatch_puts_created_match_in_onGoingGame_state_flow() = runTest {
        // Arrange
        val expectedGame = Game(localPlayerMarker = Marker.firstToMove.other, challengerBoard = GameBoard())
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any(), any()) } returns flow { emit(GameStarted(expectedGame)) }
        }
        val sut = GameViewModel(mockMatch, GameBoard())

        // Act
        val startedGate = SuspendingGate()
        var createdGame: Game? = null
        val collectJob = launch {
            sut.onGoingGame.collect {
                if (sut.state == MatchState.STARTED) {
                    createdGame = it
                    startedGate.open()
                }
            }
        }

        sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        startedGate.await()
        collectJob.cancel()

        // Assert
        Assert.assertNotNull(createdGame)
        assertEquals(expectedGame, createdGame)
        coVerify { mockMatch.start(any(), any(), any()) }
    }




    @Test
    fun a_match_cannot_be_started_while_another_one_is_running() = runTest {
        // Arrange
        val sut = GameViewModel(app.match, GameBoard())

        // Act
        val firstStart = sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        val secondStart = sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        firstStart?.cancel()

        // Assert
        Assert.assertNotNull(firstStart)
        Assert.assertNull(secondStart)
    }


    @Test
    fun takeShot_on_started_match_succeeds() = runTest {
        // Arrange
        var game = Game(localPlayerMarker = Marker.firstToMove, challengerBoard = GameBoard())
        val mockMatch: Match = mockk(relaxed = true) {
            val at = slot<Coordinate>()
            coEvery { start(any(), any(), any()) } returns flow { emit(GameStarted(game)) }
            coEvery { opponentBoardShot(capture(at), any(), any()) } answers {
                game = game.shootOpponentBoard(at.captured, GameBoard())
            }
        }

        val sut = GameViewModel(mockMatch, GameBoard())
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Act
        val at = Coordinate(line = 0, column = 0)
        sut.opponentGameBoardClickHandler(at.line, at.column, localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Assert
        Assert.assertNotNull(game.challengedBoard.cells[at.line][at.column])
        coVerify(exactly = 1) { mockMatch.start(any(), any(), any()) }
        coVerify(exactly = 1) { mockMatch.opponentBoardShot(any(), any(), any()) }
    }


    @Test
    fun forfeit_on_started_match_succeeds() = runTest {
        // Arrange
        val game = Game(localPlayerMarker = Marker.firstToMove, challengerBoard = GameBoard())
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any(), any()) } returns flow { emit(GameStarted(game)) }
            coEvery { forfeit() } returns Unit
        }

        val sut = GameViewModel(mockMatch, GameBoard())
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Act
        val job = sut.forfeit()
        job?.join()

        // Assert
        Assert.assertNotNull(job)
        coVerify(exactly = 1) { mockMatch.forfeit() }
    }

    @Test
    fun forfeiting_the_match_sets_state_to_FINISHED() = runTest {
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any(), any()) } returns flow {
                val game = Game(localPlayerMarker = Marker.firstToMove, challengerBoard = GameBoard())
                emit(GameStarted(game))
                emit(GameEnded(game.copy(forfeitedBy = Marker.firstToMove), Marker.firstToMove.other))
            }
        }

        val sut = GameViewModel(mockMatch, GameBoard())
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Assert
        assertEquals(MatchState.FINISHED, sut.state)
    }

    @Test
    fun forfeit_when_match_is_not_started_returns_null() = runTest {
        // Arrange
        val sut = GameViewModel(app.match, GameBoard())

        // Act
        val job = sut.forfeit()

        // Assert
        Assert.assertNull(job)
    }

    private val otherPlayerStartsTestChallenge = Challenge(
        challenger = otherTestPlayersInLobby.first(),
        challenged = localTestPlayer
    )

    private val localPlayerStartsTestChallenge = Challenge(
        challenger = otherTestPlayersInLobby.first(),
        challenged = localTestPlayer
    )


}