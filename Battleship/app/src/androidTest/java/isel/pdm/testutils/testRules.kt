package isel.pdm.testutils

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.BattleshipTestApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PreserveDefaultDependencies : TestRule {

    val testApplication: BattleshipTestApplication = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as BattleshipTestApplication

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val defaultUserInfoRepo = testApplication.playerRepo
                try { test.evaluate() }
                finally {
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