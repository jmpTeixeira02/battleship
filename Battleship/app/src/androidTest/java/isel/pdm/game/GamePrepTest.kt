package isel.pdm.game

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.game.prep.ui.BoardTestTag
import isel.pdm.game.prep.ui.FleetSelectorTestTag
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.game.prep.ui.GamePrepActivity.Companion.LOCAL_PLAYER
import isel.pdm.game.prep.ui.GamePrepActivity.Companion.OPPONENT_PLAYER
import isel.pdm.game.prep.ui.RandomButtonTestTag
import isel.pdm.testutils.*
import isel.pdm.ui.buttons.RemoveButtonTestTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith




@RunWith(AndroidJUnit4::class)
class GamePrepTest {


    val intent: Intent = Intent(ApplicationProvider.getApplicationContext(), GamePrepActivity::class.java)
    @get:Rule
    val testRule = createAndroidComposeRule<GamePrepActivity>(
        intent = intent.putExtra(LOCAL_PLAYER, "local").putExtra(OPPONENT_PLAYER, "opponent")
    )


    @Test
    fun screen_has_all_navigation_options() {

        // Assert
        testRule.onNodeWithTag(BoardTestTag).assertExists()
        testRule.onNodeWithTag(FleetSelectorTestTag).assertExists()
        testRule.onNodeWithTag(RandomButtonTestTag).assertExists()
        testRule.onNodeWithTag(RemoveButtonTestTag).assertExists()

    }
}