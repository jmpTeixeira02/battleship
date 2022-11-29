package isel.pdm

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BattleshipTestApplicationTests {

    @Test
    fun instrumented_tests_use_application_test_context() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("isel.pdm", context.packageName)
        Assert.assertTrue(
            "Make sure the tests runner is correctly configured in build.gradle\n" +
                    "defaultConfig { testInstrumentationRunner <test runner class name> }",
            context.applicationContext is BattleshipTestApplication
        )
    }

    @Test
    fun application_context_contains_dependencies() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertTrue(context.applicationContext is DependenciesContainer)
    }

}