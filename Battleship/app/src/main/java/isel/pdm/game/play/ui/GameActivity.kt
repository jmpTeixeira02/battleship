package isel.pdm.game.play.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.pdm.DependenciesContainer
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.model.Board
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.game.prep.ui.GamePrepActivity
import isel.pdm.game.prep.ui.GamePrepActivity.Companion.MATCH_INFO_EXTRA
import isel.pdm.game.prep.ui.MatchInfo
import isel.pdm.utils.viewModelInit
import java.util.*

class GameActivity : ComponentActivity() {


    companion object {
        private const val LOCAL_PLAYER = "local"
        const val MY_BOARD = "PREP_BOARD"
        const val OPPONENT_BOARD = "OPPONENT_PREP_BOARD"
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigate(
            origin: Activity,
            localPlayer: PlayerInfo,
            challenge: Challenge,
            prepBoard: Board,
            opponentBoard: Board
        ) {
            with(origin) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(LOCAL_PLAYER, localPlayer)
                intent.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer, challenge))
                intent.putExtra(MY_BOARD, prepBoard)
                intent.putExtra(OPPONENT_BOARD, opponentBoard)
                startActivity(intent)
            }
        }
    }


    private val viewModel: GameViewModel by viewModels {
        val app = (application as DependenciesContainer)
        val localBoard: Board = intent.getParcelableExtra(MY_BOARD)!!
        val opponentBoard: Board = intent.getParcelableExtra(OPPONENT_BOARD)!!

        viewModelInit {
            GameViewModel(app.match, localBoard.toGameBoard(), opponentBoard.toGameBoard())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentGame by viewModel.onGoingGame.collectAsState()
            val title = when (viewModel.state) {
                MatchState.STARTING -> "Joining match"
                MatchState.IDLE -> "Joining match"
                else -> null
            }
            Log.v("MATCH_INFO", "local id -> " + matchInfo.localPlayerId + "\n opponent id -> " + matchInfo.opponentId)
            GameScreen(
                players = listOf(
                    matchInfo.localPlayerNick,
                    matchInfo.opponentNick
                ),
                state = GameScreenState(title, currentGame),
                boardCellHandler = BoardCellHandler(
                    onLocalPlayerShotTaken = { line: Int, column: Int, _ ->
                        viewModel.opponentGameBoardClickHandler(line, column, localPlayer, challenge)
                    },
                    /*onOpponentPlayerShotTaken = { line: Int, column: Int, _ ->
                        viewModel.localGameBoardClickHandler(line, column, challenge)
                    },*/
                    localBoardCellList = viewModel.myCells,
                    opponentBoardCellList = viewModel.opponentCells,
                ),
                winner = viewModel.winnerFound
            )
        }

        if (viewModel.state == MatchState.IDLE)
            viewModel.startMatch(localPlayer, challenge)

        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            viewModel.forfeit()
            finish()
        }

    }

    @Suppress("deprecation")
    private val matchInfo: MatchInfo by lazy {
        val info =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfo::class.java)
            else
                intent.getParcelableExtra(MATCH_INFO_EXTRA)

        checkNotNull(info)
    }

    private val localPlayer: PlayerInfo by lazy {
        PlayerInfo(
            username = matchInfo.localPlayerNick,
            id = UUID.fromString(matchInfo.localPlayerId)
        )
    }

    private val challenge: Challenge by lazy {
        val opponent = PlayerInfo(
            username = matchInfo.opponentNick,
            id = UUID.fromString(matchInfo.opponentId)
        )

        if (localPlayer.id.toString() == matchInfo.challengerId)
            Challenge(challenger = localPlayer, challenged = opponent)
        else
            Challenge(challenger = opponent, challenged = localPlayer)
    }

}