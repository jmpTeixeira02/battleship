package isel.pdm.replay

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.selector.model.ReplayManager
import isel.pdm.replay.selector.ui.SelectReplayActivity
import isel.pdm.testutils.assertNotEmpty
import isel.pdm.testutils.isExpanded
import isel.pdm.ui.topbar.NavigateBackTestTag
import isel.pdm.ui.topbar.NavigateToAboutTestTag
import isel.pdm.ui.topbar.NavigateToEditUserTestTag
import isel.pdm.ui.topbar.NavigateToReplayTestTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SelectReplayActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<SelectReplayActivity>()

    @Test
    fun screen_only_contains_back_navigation_option() {

        // Assert
        testRule.onNodeWithTag(NavigateToEditUserTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToReplayTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToAboutTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    @Test
    fun displayed_replay_list_survives_reconfiguration() {

        // Arrange
        testRule
            .onAllNodesWithTag("ExpandableReplayView")
            .assertNotEmpty()

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule
            .onAllNodesWithTag("ExpandableReplayView")
            .assertNotEmpty()
    }

    @Test
    fun expanded_state_of_replays_is_preserved_on_reconfiguration() {

        // Arrange
        testRule
            .onAllNodesWithTag("ExpandableReplayView")
            .assertNotEmpty()
        testRule
            .onAllNodesWithTag("ExpandableReplayView.ExpandAction")
            .onFirst()
            .performClick()
        testRule.waitForIdle()
        testRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)
    }


    @Test
    fun pressing_navigate_back_finishes_activity() {

        // Arrange
        testRule.onNodeWithTag("SelectReplayScreen").assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("SelectReplayScreen").assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // THIS ONE SHOULD BE A UNIT TEST
    @Test
    fun selecting_replay_saves_and_reads_the_file() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val rep = Replay(opponentName = "opponent", shotsFired = 1)

        // Save Replay
        ReplayManager.saveReplay(context, rep)

        // Load Replay
        val loadedReplay = ReplayManager.loadReplay(context, rep.replayId)

        assert(rep.replayId == loadedReplay.replayId)
    }
}