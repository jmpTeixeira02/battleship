package isel.pdm.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.ui.screen.HomeScreen

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                replayRequest = {}
            )
        }
    }
}