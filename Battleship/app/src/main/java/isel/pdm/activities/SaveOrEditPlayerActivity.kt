package isel.pdm.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.DependenciesContainer
import isel.pdm.ui.elements.NavigationHandlers
import isel.pdm.ui.screen.CreatePlayerScreen

const val FINISH_ON_SAVE_EXTRA = "FinishOnSaveExtra"

class CreatePlayerActivity : ComponentActivity() {


    private val repo by lazy {
        (application as DependenciesContainer).playerRepo
    }

    companion object {
        fun navigate(context: Context, finishOnSave: Boolean = false) {
            with(context) {
                val intent = Intent(this, CreatePlayerActivity::class.java)
                if (finishOnSave) {
                    intent.putExtra(FINISH_ON_SAVE_EXTRA, true)
                }
                startActivity(intent)
            }
        }
    }


    /* private val viewModel: CreatePlayerViewModel by viewModels {
         viewModelInit {
             CreatePlayerViewModel()
         }
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreatePlayerScreen(
                player = repo.playerInfo,
                navigationRequest = NavigationHandlers(
                    backRequest = { finish() },
                    aboutUsRequest = { AboutUsActivity.navigate(origin = this) },
                ),
                /* playerRequest = PlayerHandler(
                     onCreatePlayer = { player: PlayerMatchmaking ->
                         viewModel.createPlayer(player)
                         HomeActivity.navigate(origin = this, player)
                     }
                 ),*/
                onSaveRequested = {
                    repo.playerInfo = it
                    if (intent.getBooleanExtra(FINISH_ON_SAVE_EXTRA, false)) {
                        finish()
                    } else {
                        LobbyActivity.navigate(this) //mudar nome da activity (player lobby)
                    }
                },
            )
            Log.v("CURR_PLAYER", repo.playerInfo.toString())

        }
    }
}