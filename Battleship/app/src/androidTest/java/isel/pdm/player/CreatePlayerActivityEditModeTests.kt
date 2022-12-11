package isel.pdm.player

import androidx.compose.ui.test.*
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import isel.pdm.activities.CreatePlayerActivity
import isel.pdm.data.player.PlayerRepository
import isel.pdm.testutils.PreserveDefaultDependencies
import isel.pdm.testutils.assertIsNotReadOnly
import isel.pdm.testutils.createPreserveDefaultDependenciesComposeRule
import isel.pdm.ui.elements.NavigateBackTestTag
import isel.pdm.ui.elements.NavigateToAboutTestTag
import isel.pdm.ui.elements.NavigateToEditUserTestTag
import isel.pdm.ui.elements.NavigateToReplayTestTag
import isel.pdm.ui.elements.buttons.SaveButtonTag
import isel.pdm.ui.screen.CreatePlayerScreenTag
import isel.pdm.ui.screen.HomeScreenTag
import isel.pdm.ui.screen.UsernameInputTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePlayerActivityEditModeTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private val mockRepo: PlayerRepository = mockk(relaxed = true) {
        every { playerInfo } returns null
    }

    @Test
    fun screen_has_back_and_about_navigation_options() {

        // Assert
        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
            testRule.onNodeWithTag(NavigateToAboutTestTag).assertExists()
            testRule.onNodeWithTag(NavigateToReplayTestTag).assertDoesNotExist()
            testRule.onNodeWithTag(NavigateToEditUserTestTag).assertDoesNotExist()
        }
    }


    @Test
    fun screen_create_player_button_is_disabled_if_text_field_is_empty() {
        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
        }
    }

    @Test
    fun screen_create_player_button_is_disabled_if_entered_info_is_invalid() {

        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(UsernameInputTag).performTextInput("\n  \t ")
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
        }
    }

    @Test
    fun screen_save_player_button_is_disabled_if_username_bigger_than_15_chars() {

        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(UsernameInputTag).performTextInput("abcdefghijklmnop")
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
        }
    }

    @Test
    fun screen_save_player_button_is_enabled_if_username_is_valid() {

        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(UsernameInputTag).performTextInput("abcdefghijkl")
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsEnabled()
        }
    }

    @Test
    fun screen_textFields_do_accept_user_input() {

        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(UsernameInputTag).assertIsNotReadOnly()
        }
    }

    @Test
    fun pressing_save_button_stores_info_and_navigates_to_home_screen() {

        application.playerRepo = mockRepo

        ActivityScenario.launch(CreatePlayerActivity::class.java).use {
            testRule.onNodeWithTag(UsernameInputTag).performTextInput("newUser")
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            testRule.waitForIdle()

            // Assert
            verify { mockRepo.playerInfo }
            testRule.onNodeWithTag(CreatePlayerScreenTag).assertDoesNotExist()
            testRule.onNodeWithTag(HomeScreenTag).assertExists()
        }
    }

}