package isel.pdm.game.play

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.*
import isel.pdm.game.play.ui.GameActivity
import isel.pdm.game.prep.ui.MatchInfo
import isel.pdm.localTestPlayer
import isel.pdm.otherTestPlayersInLobby
import isel.pdm.testutils.PreserveDefaultDependencies
import isel.pdm.testutils.createPreserveDefaultDependenciesComposeRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import isel.pdm.R
import isel.pdm.game.lobby.model.firstToMove
import isel.pdm.game.play.ui.ForfeitButtonTag
import isel.pdm.game.play.ui.GameScreenTestTag
import isel.pdm.game.play.ui.GameScreenTitleTag
import isel.pdm.game.prep.model.Board
import isel.pdm.game.prep.model.Cell
import isel.pdm.ui.BoardCellTestTag
import isel.pdm.ui.CellViewTag
import isel.pdm.ui.OpponentBoardCellTestTag
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.runner.RunWith


private const val STARTUP_DELAY = 5000L

@RunWith(AndroidJUnit4::class)
class GameActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val delayedMockMatch: Match = createMockMatch(STARTUP_DELAY)
    private val immediateMockMatch: Match = createMockMatch()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private fun createMockMatch(delayMs: Long? = null): Match = mockk(relaxed = true) {
        val localPlayer = slot<PlayerInfo>()
        val challenge = slot<Challenge>()
        val localGameBoard = slot<GameBoard>()
        coEvery { start(capture(localPlayer), capture(challenge), capture(localGameBoard)) } answers {
            flow {
                if (delayMs != null)
                    delay(delayMs)
                val localMarker = getLocalPlayerMarker(localPlayer.captured, challenge.captured)
                emit(GameStarted(Game(localPlayerMarker = localMarker, challengerBoard = GameBoard())))
            }
        }
        coEvery { (opponentBoardShot(any(), any(), any())) } returns Unit
        coEvery { forfeit() } returns Unit
    }

    private fun createMatchIntent(localPLayerStarts: Boolean): Pair<Intent, Challenge> {
        val challenge =
            if (localPLayerStarts)
                Challenge(
                    challenger = localTestPlayer,
                    challenged = otherTestPlayersInLobby.first()
                )
            else
                Challenge(
                    challenger = otherTestPlayersInLobby.first(),
                    challenged = localTestPlayer
                )
        val intent = Intent(application, GameActivity::class.java).also {
            it.putExtra(GameActivity.MATCH_INFO_EXTRA, MatchInfo(localTestPlayer, challenge))
            it.putExtra(GameActivity.MY_BOARD, Board())
        }
        return Pair(intent, challenge)
    }

    @Test
    fun game_activity_starts_by_displaying_starting_dialog() {

        // Arrange
        application.match = delayedMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            testRule.onNodeWithTag(GameScreenTestTag).assertExists()
            testRule
                .onNodeWithTag(GameScreenTitleTag)
                .assertTextEquals(application.getString(R.string.game_screen_waiting))
        }
    }

    @Test
    fun when_its_local_player_turn_board_is_enabled() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            assertEquals(challenge.firstToMove, localTestPlayer)
            testRule
                .onAllNodesWithTag(CellViewTag)
                .assertAll(isEnabled())
        }
    }

    @Test
    fun when_its_opponent_player_turn_board_is_disabled() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            assertEquals(challenge.challenged, localTestPlayer)
            testRule
                .onAllNodesWithTag(CellViewTag)
                .assertAll(isNotEnabled())
        }
    }


    @Test
    fun when_its_local_player_turn_board_clicks_takes_shot() {

        // Arrange
        application.match = immediateMockMatch
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onAllNodesWithTag(OpponentBoardCellTestTag())
                .onFirst()
                .performClick()

            testRule.waitForIdle()

            // Assert
            assertEquals(challenge.firstToMove, localTestPlayer)
            coVerify(exactly = 1) { immediateMockMatch.opponentBoardShot(any(), localTestPlayer, challenge ) }
        }
    }

    @Test
    fun when_its_remote_player_turn_board_is_disabled() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            Assert.assertNotEquals(challenge.firstToMove, localTestPlayer)
            testRule
                .onAllNodesWithTag(OpponentBoardCellTestTag())
                .onFirst()
                .performClick()

        }
    }

    @Test
    fun when_its_remote_player_turn_board_clicks_do_not_make_moves() {

        // Arrange
        application.match = immediateMockMatch
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onAllNodesWithTag(OpponentBoardCellTestTag())
                .onFirst()
                .performClick()

            testRule.waitForIdle()

            // Assert
            Assert.assertNotEquals(challenge.firstToMove, localTestPlayer)
            coVerify(exactly = 0) { immediateMockMatch.opponentBoardShot(any(), localTestPlayer, challenge ) }
        }
    }


    @Test
    fun when_game_ends_match_end_dialog_is_shown() {
        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)
        val localMarker = getLocalPlayerMarker(localTestPlayer, challenge)
        application.match = mockk {
            var result: BoardResult? = null
            coEvery { start(any(), any(), any()) } answers {
                flow {
                    val game = Game(localPlayerMarker = localMarker, challengerBoard = GameBoard())
                    emit(GameStarted(game))
                    while (result != null)
                        delay(1000)
                    emit(GameEnded(game = game, winner = localMarker.other))
                }
            }
            coEvery { opponentBoardShot(any(), any(), any()) } returns Unit
            coEvery { forfeit() } answers { result = HasWinner(winner = localMarker.other) }
            coEvery { end() } returns Unit
        }

        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onNodeWithTag(ForfeitButtonTag)
                .performClick()

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(GameScreenTitleTag).assertExists()
            testRule
                .onNodeWithTag(GameScreenTitleTag)
                .assertTextEquals(application.getString(R.string.match_ended_dialog_text_lose))

        }
    }

    @Test
    fun when_forfeit_button_is_pressed_forfeit_is_called() {
        // Arrange
        application.match = immediateMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onNodeWithTag(ForfeitButtonTag)
                .performClick()

            testRule.waitForIdle()

            // Assert
            coVerify(exactly = 1) { immediateMockMatch.forfeit() }
        }
    }

    @Test
    fun back_navigation_when_match_is_ongoing_forfeits_it() {
        // Arrange
        application.match = immediateMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            it.onActivity { activity -> activity.onBackPressedDispatcher.onBackPressed() }
            testRule.waitForIdle()

            // Assert
            coVerify(exactly = 1) { immediateMockMatch.forfeit() }
        }
    }

}