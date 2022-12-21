package isel.pdm.game.play.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.play.model.Game
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.play.model.Marker
import isel.pdm.game.prep.model.Board
import isel.pdm.game.prep.ui.BoardCellHandler
import isel.pdm.utils.viewModelInit

class GameActivity : ComponentActivity() {


    companion object {
        const val LOCAL_PLAYER = "local"
        const val OPPONENT_PLAYER = "OPPONENT"
        const val MY_BOARD = "PREP_BOARD"
        const val OPPONENT_BOARD = "OPPONENT_PREP_BOARD"
        fun navigate(origin: Activity, local: String, opponent: String, prepBoard: Board, opponentBoard : Board) {
            with(origin) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(LOCAL_PLAYER, local)
                intent.putExtra(OPPONENT_PLAYER, opponent)
                intent.putExtra(MY_BOARD, prepBoard)
                intent.putExtra(OPPONENT_BOARD, prepBoard)
                startActivity(intent)
            }
        }
    }


    private val viewModel: GameViewModel by viewModels {
        val localBoard: Board = intent.getParcelableExtra(MY_BOARD)!!
        val opponentBoard: Board = intent.getParcelableExtra(OPPONENT_BOARD)!!
        viewModelInit {
            GameViewModel(localBoard.toGameBoard(), opponentBoard.toGameBoard())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val opponent: String = intent.getStringExtra(OPPONENT_PLAYER)!!
        val local: String = intent.getStringExtra(LOCAL_PLAYER)!!

        setContent {
            val game = Game(Marker.LOCAL, GameBoard())
            GameScreen(
                state = GameScreenState(game),
                players = listOf(
                    PlayerMatchmaking(local),
                    PlayerMatchmaking(opponent)
                ),
                boardCellHandler = BoardCellHandler(
                    onCellClick = { line: Int, column: Int, _ ->
                        viewModel.gameBoardClickHandler(line, column)
                    },
                    boardCellList = viewModel.myCells,
                ),
                //myPrepBoard = prepBoard
            )
        }
    }


}