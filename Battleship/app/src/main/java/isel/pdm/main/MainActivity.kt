package isel.pdm.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.DependenciesContainer
import isel.pdm.game.lobby.ui.LobbyActivity
import isel.pdm.preferences.ui.CreatePlayerActivity

class MainActivity : ComponentActivity() {
    private val repo by lazy {
        (application as DependenciesContainer).playerRepo
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onStartRequested = ::startGame)
        }
    }

    private fun startGame() {
        if (repo.playerInfo != null)
            LobbyActivity.navigate(this, repo.playerInfo)
        else
            CreatePlayerActivity.navigate(this)
    }
}