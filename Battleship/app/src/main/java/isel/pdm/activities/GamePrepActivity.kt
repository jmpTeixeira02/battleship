package isel.pdm.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.R
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.screen.AboutUsScreen
import isel.pdm.ui.screen.GamePrepScreen

class GamePrepActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, GamePrepActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamePrepScreen(
                emptyList()
            )
        }
    }
}