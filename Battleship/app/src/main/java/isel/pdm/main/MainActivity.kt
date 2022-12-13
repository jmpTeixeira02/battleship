package isel.pdm.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.DependenciesContainer
import isel.pdm.preferences.ui.CreatePlayerActivity
import isel.pdm.game.lobby.ui.LobbyActivity

class MainActivity : ComponentActivity() {
    private val repo by lazy {
        (application as DependenciesContainer).playerRepo
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