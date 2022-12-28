package isel.pdm.player

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import isel.pdm.preferences.ui.CreatePlayerActivity
import isel.pdm.testutils.assertIsReadOnly
import isel.pdm.ui.topbar.NavigateBackTestTag
import isel.pdm.ui.buttons.EditButtonTag
import isel.pdm.ui.buttons.SaveButtonTag
import isel.pdm.preferences.ui.CreatePlayerScreenTag
import isel.pdm.preferences.ui.UsernameInputTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePlayerActivityReadOnlyModeTests {

    @get:Rule
    val testRule = createAndroidComposeRule<CreatePlayerActivity>()

    @Test
    fun preferences_screen_is_displayed() {
        testRule.onNodeWithTag(CreatePlayerScreenTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(CreatePlayerScreenTag).assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun screen_has_edit_button_if_user_info_exists() {
        testRule.onNodeWithTag(EditButtonTag).assertExists()
    }

    @Test
    fun screen_textFields_do_not_accept_user_input() {
        testRule.onNodeWithTag(UsernameInputTag).assertIsReadOnly()
    }

    @Test
    fun pressing_edit_button_places_screen_in_edit_mode() {

        // Act
        testRule.onNodeWithTag(EditButtonTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(EditButtonTag).assertDoesNotExist()
        testRule.onNodeWithTag(SaveButtonTag).assertExists()
    }

}