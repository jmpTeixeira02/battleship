package isel.pdm.replay

import android.content.Intent
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import isel.pdm.game.play.model.GameBoard
import isel.pdm.game.prep.model.*
import isel.pdm.replay.viewer.model.GameInfo
import isel.pdm.replay.selector.model.Replay
import isel.pdm.replay.viewer.ui.*
import isel.pdm.replay.viewer.ui.ReplayGameActivity.Companion.REPLAY_EXTRA
import isel.pdm.testutils.createAndroidComposeRule
import isel.pdm.testutils.performClickAndWaitForIdle
import isel.pdm.ui.MyGameBoard
import isel.pdm.ui.OpponentGameBoard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewReplayActivityTests {


    val intent: Intent =
        Intent(ApplicationProvider.getApplicationContext(), ReplayGameActivity::class.java)

    private fun movesInMyTestBoard(myBoard: GameBoard): GameBoard{
        myBoard.cells[1][2] = Cell(ship = Ship(TypeOfShip.Destroyer))
        myBoard.cells[1][1] = Cell(ship = Ship(TypeOfShip.Carrier))
        myBoard.cells[1][3] = Cell()
        return myBoard
    }

    private fun movesInOpponentTestBoard(opponentBoard: GameBoard): GameBoard{
        opponentBoard.cells[1][2] = Cell(ship = Ship(TypeOfShip.Destroyer), state = BiStateGameCellShot.HasBeenShot)
        opponentBoard.cells[1][1] = Cell(ship = Ship(TypeOfShip.Carrier), state = BiStateGameCellShot.HasBeenShot)
        opponentBoard.cells[1][3] = Cell(state = BiStateGameCellShot.HasBeenShot)
        return opponentBoard
    }

    val testReplay: Replay = Replay(
        replayId = "#123",
        date = "01/01/01",
        opponentName = "Test",
        shotsFired = 17,
        gameInfo = GameInfo(
            myBoard = movesInMyTestBoard(GameBoard()),
            opponentBoard = movesInOpponentTestBoard(GameBoard()),
            myMoves = listOf(Coordinate(1,1), Coordinate(1,2), Coordinate(1,3)),
            opponentMoves = listOf(Coordinate(1,1), Coordinate(1,2),Coordinate(1,3)),
            iMadeFirstMove = true
        )
    )

    @get:Rule
    val testRule = createAndroidComposeRule<ReplayGameActivity>(
        intent = intent.putExtra(REPLAY_EXTRA, testReplay)
    )

    @Test
    fun screen_has_all_navigation_options() {
        // Assert
        testRule.onNodeWithTag(MoveCounter).assertExists()
        testRule.onNodeWithTag(ForwardMoveButton).assertExists()
        testRule.onNodeWithTag(BackwardMoveButton).assertExists()
        testRule.onNodeWithTag(MyGameBoard).assertExists()
    }

    @Test
    fun move_counter_starts_at_0(){
        val vm = testRule.activity.viewModel
        assert(vm.moveCounter == 0)
    }

    @Test
    fun move_counter_cannot_be_negative(){
        val vm = testRule.activity.viewModel
        assert(vm.moveCounter == 0)
        testRule.performClickAndWaitForIdle(BackwardMoveButton)
        assert(vm.moveCounter == 0)
    }

    @Test
    fun move_counter_cannot_be_higher_then_the_sum_of_plays(){
        val vm = testRule.activity.viewModel
        val sumOfMoves = testReplay.gameInfo.myMoves.size + testReplay.gameInfo.opponentMoves.size
        repeat(sumOfMoves + 3){
            testRule.performClickAndWaitForIdle(ForwardMoveButton)
        }
        assert(vm.moveCounter == sumOfMoves)
    }

    @Test
    fun my_move_affects_opponent_board(){
        val vm = testRule.activity.viewModel
        val myMoves = testReplay.gameInfo.myMoves
        val opponentCell = testReplay.gameInfo.opponentBoard.cells
        testRule.performClickAndWaitForIdle(ForwardMoveButton)
        assert(vm.opponentReplayCells[myMoves[0].line][myMoves[0].column] == opponentCell[myMoves[0].line][myMoves[0].column])
    }

    @Test
    fun opponent_move_affects_my_board(){
        val vm = testRule.activity.viewModel
        val opponentMoves = testReplay.gameInfo.opponentMoves
        val myCell = testReplay.gameInfo.myBoard.cells
        testRule.performClickAndWaitForIdle(ForwardMoveButton)
        assert(vm.myReplayCells[opponentMoves[0].line][opponentMoves[0].column] == myCell[opponentMoves[0].line][opponentMoves[0].column])
    }
}