package isel.pdm.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.R
import isel.pdm.ui.screen.AboutUsScreen
import isel.pdm.ui.screen.HomeScreen

class AboutUsActivity : ComponentActivity(){

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutUsScreen(
                backRequest = {finish()},
                sendEmailRequest = {sendEmail()},
                authors = authors
            )
        }
    }

    private fun sendEmail(): Unit{
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, authorsEmail)
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }

            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}

private val authors = listOf<String>("A1", "A2", "A3")
private val authorsEmail = arrayOf<String>("a1@email.com, a2@email.com, a3@email.com")
private const val emailSubject = "Battleship App"

class MainActivity : ComponentActivity() {
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