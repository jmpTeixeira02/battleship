package isel.pdm.game.prep.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.game.lobby.model.Challenge
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.play.model.FakeOpponent
import isel.pdm.game.play.ui.GameActivity
import isel.pdm.game.prep.model.Board
import isel.pdm.game.prep.model.Ship
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.main.MainActivity
import isel.pdm.ui.buttons.BiState
import isel.pdm.utils.viewModelInit
import kotlinx.parcelize.Parcelize

class GamePrepActivity : ComponentActivity() {

    companion object {
        const val LOCAL_PLAYER = "local"
        const val OPPONENT_PLAYER = "OPPONENT"
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigate(origin: Context, localPlayer: PlayerInfo, challenge: Challenge) {
            with(origin) {
                startActivity(
                    Intent(this, GamePrepActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer, challenge))
                    }
                )
            }
        }
    }

    val viewModel: GamePrepViewModel by viewModels {
        viewModelInit {
            GamePrepViewModel()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val matchInfo: MatchInfo = intent.getParcelableExtra(MATCH_INFO_EXTRA)!!

        setContent {
            val deleteButtonState =
                if (viewModel.isDeleting) BiState.hasBeenPressed
                else BiState.hasNotBeenPressed
            GamePrepScreen(
                players = listOf(
                    matchInfo.localPlayerNick,
                    matchInfo.opponentNick
                ),
                shipRemoverHandler = ShipRemoverHandler(
                    deleteButtonState = deleteButtonState,
                    onDeleteButtonClick = { viewModel.deleteBoatToggle() },
                ),
                onRandomShipPlacer = { viewModel.randomFleet() },
                boardCellHandler = BoardCellHandler(
                    onCellClick = { line: Int, column: Int, selectedShip: Ship? ->
                        viewModel.boardClickHandler(line, column, selectedShip)
                    },
                    boardCellList = viewModel.boardCells,
                ),
                shipSelectionHandler = ShipSelectionHandler(
                    selectedShip = viewModel.shipSelector
                        .filterValues { e: ShipState -> e == ShipState.isSelected }.keys.firstOrNull(),
                    shipSelector = viewModel.shipSelector,
                    onShipSelectorClick = { boatSelected: TypeOfShip ->
                        viewModel.shipSelectorHandler(boatSelected)
                    }
                ),
                onCheckBoardPrepRequest = ::checkBoardPrepState
            )
        }

        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            MainActivity.navigate(this@GamePrepActivity)
            finish()
        }

    }

    private fun checkBoardPrepState() {
        if (viewModel.allShipsPlaced()) { /* time's up e barcos postos totalmente*/
            val matchInfo: MatchInfo = intent.getParcelableExtra(MATCH_INFO_EXTRA)!!

            val prepBoard = viewModel.getBoard()

            GameActivity.navigate(
                this,
                localPlayer = PlayerInfo(matchInfo.localPlayerNick),
                challenge = Challenge(PlayerInfo(matchInfo.localPlayerNick), PlayerInfo(matchInfo.opponentNick)),
                prepBoard,
                prepBoard
            )
        } else { /* time's up e barcos não postos totalmente */
            MainActivity.navigate(this)
        }
    }
}


@Parcelize
data class MatchInfo(
    val localPlayerId: String,
    val localPlayerNick: String,
    val opponentId: String,
    val opponentNick: String,
    val challengerId: String,
) : Parcelable

internal fun MatchInfo(localPlayer: PlayerInfo, challenge: Challenge): MatchInfo {
    val opponent =
        if (localPlayer == challenge.challenged) challenge.challenger
        else challenge.challenged

    return MatchInfo(
        localPlayerId = localPlayer.id.toString(),
        localPlayerNick = localPlayer.username,
        opponentId = opponent.id.toString(),
        opponentNick = opponent.username,
        challengerId = challenge.challenger.id.toString(),
    )
}