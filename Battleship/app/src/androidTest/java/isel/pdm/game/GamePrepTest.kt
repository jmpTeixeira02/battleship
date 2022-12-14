package isel.pdm.game

import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.game.prep.model.Cell
import isel.pdm.game.prep.model.TypeOfShip
import isel.pdm.game.prep.ui.*
import isel.pdm.game.prep.ui.GamePrepActivity.Companion.LOCAL_PLAYER
import isel.pdm.game.prep.ui.GamePrepActivity.Companion.OPPONENT_PLAYER
import isel.pdm.testutils.*
import isel.pdm.ui.buttons.RemoveButtonTestTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith




@RunWith(AndroidJUnit4::class)
class GamePrepTest {


    val intent: Intent =
        Intent(ApplicationProvider.getApplicationContext(), GamePrepActivity::class.java)

    @get:Rule
    val testRule = createAndroidComposeRule<GamePrepActivity>(
        intent = intent.putExtra(LOCAL_PLAYER, "local").putExtra(OPPONENT_PLAYER, "opponent")
    )

    @Test
    fun screen_has_all_navigation_options() {
        // Assert
        testRule.onNodeWithTag(BoardTestTag).assertExists()
        testRule.onNodeWithTag(FleetSelectorTestTag).assertExists()
        testRule.onNodeWithTag(RandomButtonTestTag).assertExists()
        testRule.onNodeWithTag(RemoveButtonTestTag).assertExists()
    }

    @Test
    fun ship_selector_starts_empty() {
        val vm = testRule.activity.viewModel

        assert(vm.shipSelector.values.count { e -> e == ShipState.isNotSelected } == vm.shipSelector.values.size)
    }

    @Test
    fun ship_selector_select_ship() {
        val vm = testRule.activity.viewModel

        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)

        assert(vm.shipSelector[TypeOfShip.Destroyer] == ShipState.isSelected)
    }

    @Test
    fun ship_selector_swap_selected_ship() {
        val vm = testRule.activity.viewModel

        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(FleetSelectorBattleShipTestTag)

        assert(
            vm.shipSelector[TypeOfShip.Destroyer] == ShipState.isNotSelected &&
                    vm.shipSelector[TypeOfShip.BattleShip] == ShipState.isSelected
        )
    }

    @Test
    fun board_starts_empty() {
        val vm = testRule.activity.viewModel

        assert(
            vm.boardCells.flatten().count { e -> e.ship == null } ==
                    vm.boardCells.flatten().size
        )
    }

    @Test
    fun valid_place_ship_on_board() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 9))

        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Destroyer } ==
                    TypeOfShip.Destroyer.size
        )
    }

    @Test
    fun ship_has_no_space_on_the_board() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 2))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))

        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    1
        )
    }

    @Test
    fun ship_starts_on_another_ship() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 2))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    TypeOfShip.Carrier.size
        )

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorCruiserTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 1))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Cruiser } ==
                    0
        )
    }

    @Test
    fun ship_placing_overlaps_existing_ship() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 2))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    TypeOfShip.Carrier.size
        )

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorBattleShipTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(3, 1))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(5, 1))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.BattleShip } ==
                    1
        )
    }

    @Test
    fun trying_to_place_ship_diagonally() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(5, 1))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    1
        )
    }

    @Test
    fun after_invalid_ship_placement_try_to_do_a_valid_place() {
        val vm = testRule.activity.viewModel

        // Place ship Start
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 0))

        // Invalid ship end
        testRule.performClickAndWaitForIdle(BoardCellTestTag(5, 1))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    1
        )

        // Valid ship end
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 1))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    TypeOfShip.Carrier.size
        )
    }

    @Test
    fun swap_selected_ship_during_ship_placement() {
        val vm = testRule.activity.viewModel

        // Place ship starter
        testRule.performClickAndWaitForIdle(FleetSelectorCarrierTestTag)
        assert(vm.shipSelector[TypeOfShip.Carrier] == ShipState.isSelected)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(4, 0))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    1
        )

        // Swap selected ship
        testRule.performClickAndWaitForIdle(FleetSelectorCruiserTestTag)
        assert(
            vm.shipSelector[TypeOfShip.Carrier] == ShipState.isNotSelected &&
                    vm.shipSelector[TypeOfShip.Cruiser] == ShipState.isSelected
        )
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Carrier } ==
                    0
        )
    }

    @Test
    fun ship_selector_ship_gets_placed_after_placing_ship_on_board() {
        val vm = testRule.activity.viewModel

        // Place ship
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 9))

        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Destroyer } ==
                    TypeOfShip.Destroyer.size
        )

        assert(vm.shipSelector[TypeOfShip.Destroyer] == ShipState.hasBeenPlaced)
    }

    @Test
    fun delete_button_resets_selected_ship_but_maintains_placed_ship() {
        val vm = testRule.activity.viewModel

        // Place Ship
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 9))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Destroyer } ==
                    TypeOfShip.Destroyer.size
        )
        assert(vm.shipSelector[TypeOfShip.Destroyer] == ShipState.hasBeenPlaced)

        // Place Ship Starter
        testRule.performClickAndWaitForIdle(FleetSelectorCruiserTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(9, 9))
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Cruiser } ==
                    1
        )
        assert(vm.shipSelector[TypeOfShip.Cruiser] == ShipState.isSelected)

        // Remove Button Clicked
        testRule.performClickAndWaitForIdle(RemoveButtonTestTag)
        assert(
            vm.shipSelector[TypeOfShip.Cruiser] == ShipState.isNotSelected &&
                    vm.shipSelector[TypeOfShip.Destroyer] == ShipState.hasBeenPlaced
        )
        assert(
            vm.boardCells.flatten().count { e ->
                e.ship?.type == TypeOfShip.Destroyer
            } == TypeOfShip.Destroyer.size &&
                    vm.boardCells.flatten().count { e ->
                        e.ship?.type == TypeOfShip.Cruiser
                    } == 0
        )
    }

    @Test
    fun remove_ship_from_board_click() {
        val vm = testRule.activity.viewModel

        // Place Ship
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 9))

        // Remove Button Clicked
        testRule.performClickAndWaitForIdle(RemoveButtonTestTag)

        // Select ship from board
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))

        assert(vm.shipSelector[TypeOfShip.Destroyer] == ShipState.isNotSelected)
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Destroyer } ==
                    0
        )
    }

    @Test
    fun remove_ship_from_selector_click() {
        val vm = testRule.activity.viewModel

        // Place Ship
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 0))
        testRule.performClickAndWaitForIdle(BoardCellTestTag(0, 9))

        // Remove Button Clicked
        testRule.performClickAndWaitForIdle(RemoveButtonTestTag)

        // Select ship from board
        testRule.performClickAndWaitForIdle(FleetSelectorDestoyerTestTag)

        assert(vm.shipSelector[TypeOfShip.Destroyer] == ShipState.isNotSelected)
        assert(
            vm.boardCells.flatten().count { e -> e.ship?.type == TypeOfShip.Destroyer } ==
                    0
        )
    }

    @Test
    fun random_button_places_all_ships(){
        val vm = testRule.activity.viewModel

        // Press random button
        testRule.performClickAndWaitForIdle(RandomButtonTestTag)

        assert(
            vm.shipSelector.values.count { e -> e == ShipState.hasBeenPlaced } ==
            vm.shipSelector.values.size
        )
    }
}