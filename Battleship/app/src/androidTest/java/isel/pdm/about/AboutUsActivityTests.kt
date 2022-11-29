package isel.pdm.about

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import isel.pdm.activities.AboutUsActivity
import isel.pdm.ui.elements.NavigateBackTestTag
import isel.pdm.ui.elements.NavigateToAboutTestTag
import isel.pdm.ui.elements.NavigateToReplayTestTag
import org.junit.Rule
import org.junit.Test

class AboutUsActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<AboutUsActivity>()

    @Test
    fun screen_only_contains_back_navigation_option() {

        // Assert
        testRule.onNodeWithTag(NavigateToReplayTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToAboutTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        // Arrange
        testRule.onNodeWithTag("AboutScreen").assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("AboutScreen").assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }
}