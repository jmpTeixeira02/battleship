package isel.pdm.home

import android.content.Intent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import isel.pdm.game.lobby.ui.LobbyActivity
import isel.pdm.game.lobby.ui.LobbyScreenTag
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.preferences.ui.CreatePlayerScreenTag
import isel.pdm.testutils.assertNotEmpty
import isel.pdm.ui.topbar.NavigateBackTestTag
import isel.pdm.ui.topbar.NavigateToAboutTestTag
import isel.pdm.ui.topbar.NavigateToEditUserTestTag
import isel.pdm.ui.topbar.NavigateToReplayTestTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LobbyActivityTests {


    val intent: Intent = Intent(ApplicationProvider.getApplicationContext(), LobbyActivity::class.java)
    @get:Rule
    val testRule = isel.pdm.testutils.createAndroidComposeRule<LobbyActivity>(
        intent = intent.putExtra(LobbyActivity.LOCAL_PLAYER, "local")
    )


    @Test
    fun screen_has_all_navigation_options() {

        // Assert
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToAboutTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToReplayTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToEditUserTestTag).assertExists()

    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(LobbyScreenTag).assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun displayed_players_survive_reconfiguration() {

        // Arrange
        testRule.onAllNodesWithTag("PlayerView").assertNotEmpty()

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule.onAllNodesWithTag("PlayerView").assertNotEmpty()
    }
}