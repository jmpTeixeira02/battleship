package isel.pdm.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import isel.pdm.game.lobby.ui.LobbyActivity
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

    @get:Rule
    val testRule = createAndroidComposeRule<LobbyActivity>()


    @Test
    fun screen_has_all_navigation_options() {

        // Assert
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToAboutTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToReplayTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToEditUserTestTag).assertExists()

    }

    @Test
    fun refresh_button_shows_available_players() {

        // Arrange
        testRule.onNodeWithTag("PlayerView").assertDoesNotExist()

        testRule.onNodeWithTag("LoadingButton").performClick()
        testRule.waitForIdle()


        // Assert
        testRule.onAllNodesWithTag("PlayerView").assertNotEmpty()
    }


    @Test
    fun displayed_players_survive_reconfiguration() {

        testRule.onNodeWithTag("PlayerView").assertDoesNotExist()

        testRule.onNodeWithTag("LoadingButton").performClick()
        testRule.waitForIdle()

        // Arrange
        testRule.onAllNodesWithTag("PlayerView").assertNotEmpty()

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule.onAllNodesWithTag("PlayerView").assertNotEmpty()
    }



}