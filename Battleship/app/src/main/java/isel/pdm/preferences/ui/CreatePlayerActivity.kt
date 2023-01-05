package isel.pdm.preferences.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.DependenciesContainer
import isel.pdm.game.lobby.ui.LobbyActivity
import isel.pdm.info.AboutUsActivity
import isel.pdm.ui.topbar.NavigationHandlers

class CreatePlayerActivity : ComponentActivity() {


    private val repo by lazy {
        (application as DependenciesContainer).playerRepo
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, CreatePlayerActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreatePlayerScreen(
                player = repo.playerInfo,
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                ),
                onSaveRequested = {
                    repo.playerInfo = it
                    LobbyActivity.navigate(this, player = it)
                },
            )
        }
    }
}