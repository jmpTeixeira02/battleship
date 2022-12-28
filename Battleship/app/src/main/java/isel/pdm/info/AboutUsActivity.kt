package isel.pdm.info

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
import isel.pdm.ui.topbar.NavigationHandlers

class AboutUsActivity : ComponentActivity() {

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
                navigationRequest = NavigationHandlers(backRequest = {finish()}),
                sendEmailRequest = { sendEmail() },
                authors = authors
            )
        }
    }

    private fun sendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, authorsEmail)
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }

            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
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

private val authors =
    listOf<String>("João Teixeira A48710", "João Cravo A46109", "João Martins A50055")
private val authorsEmail =
    arrayOf<String>("a48710@alunos.isel.pt, a46109@alunos.isel.pt, a50055@alunos.isel.pt")
private const val emailSubject = "Battleship App"

