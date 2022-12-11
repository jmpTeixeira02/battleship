package isel.pdm.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.DependenciesContainer
import isel.pdm.ui.screen.MainScreen

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
            LobbyActivity.navigate(this)
        else
            CreatePlayerActivity.navigate(this)
    }
}