package isel.pdm.testutils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.BattleshipTestApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Creates a test rule that starts an activity of type <A> with the received
 * intent.
 */
fun <A : ComponentActivity> createAndroidComposeRule(
    intent: Intent
): AndroidComposeTestRule<ActivityScenarioRule<A>, A> = AndroidComposeTestRule(
    activityRule = ActivityScenarioRule(intent),
    activityProvider = { rule ->
        var activity: A? = null
        rule.scenario.onActivity { activity = it }
        activity!!
    }
)

class PreserveDefaultDependencies : TestRule {

    val testApplication: BattleshipTestApplication = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as BattleshipTestApplication

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val defaultUserInfoRepo = testApplication.playerRepo
                try {
                    test.evaluate()
                } finally {
                    testApplication.playerRepo = defaultUserInfoRepo
                }
            }
        }
}

fun createPreserveDefaultDependenciesComposeRule() =
    AndroidComposeTestRule<TestRule, ComponentActivity>(
        activityRule = PreserveDefaultDependencies(),
        activityProvider = {
            error("This rule does not provide an Activity. Launch and use the Activity yourself.")
        }
    )


fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.performClickAndWaitForIdle(
    testTag: String
) {
    this.onNodeWithTag(testTag).performClick()
    this.waitForIdle()
}

fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.performClickAndWaitForTimer(
    testTag: String,
    milliseconds: Long,
) {
    this.onNodeWithTag(testTag).performClick()
    Thread.sleep(milliseconds)
}