package isel.pdm.ui.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.utils.drawCell

enum class FleetSelector(val size: Int){
    Destroyer(2),
    Submarine(3),
    Cruiser(3),
    BattleShip( 4),
    Carrier(5)
}

enum class BoatSelector {hasBeenPlaced, isSelected, isNotSelected}

@Composable
fun FleetSelectorView(
    modifier: Modifier = Modifier,
    onClick: (idx: Int) -> Unit = { _->},
    selectedBoatStateList: List<BoatSelector> = List(FleetSelector.values().size){ _ -> BoatSelector.isNotSelected}
){
    BoxWithConstraints(modifier = modifier.padding(4.dp)) {
        val maxBoatCellsSize = this.maxWidth / 6
        val spaceModifier = Modifier
            .height(4.dp)
            .width(4.dp)
        Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                buildBoatSelect(
                    size = FleetSelector.Destroyer.size, name = FleetSelector.Destroyer.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[FleetSelector.Destroyer.ordinal],
                    listIdx = FleetSelector.Destroyer.ordinal
                )
                Spacer(modifier = spaceModifier)
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    buildBoatSelect(
                        size = FleetSelector.Submarine.size, name = FleetSelector.Submarine.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = selectedBoatStateList[FleetSelector.Submarine.ordinal],
                        listIdx = FleetSelector.Submarine.ordinal
                    )
                    Spacer(modifier = spaceModifier)
                    buildBoatSelect(
                        size = FleetSelector.Cruiser.size, name = FleetSelector.Cruiser.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = selectedBoatStateList[FleetSelector.Cruiser.ordinal],
                        listIdx = FleetSelector.Cruiser.ordinal
                    )
                }
                Spacer(modifier = spaceModifier)
                buildBoatSelect(
                    size = FleetSelector.BattleShip.size, name = FleetSelector.BattleShip.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[FleetSelector.BattleShip.ordinal],
                    listIdx = FleetSelector.BattleShip.ordinal
                )
                Spacer(modifier = spaceModifier)
                buildBoatSelect(
                    size = FleetSelector.Carrier.size, name = FleetSelector.Carrier.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[FleetSelector.Carrier.ordinal],
                    listIdx = FleetSelector.Carrier.ordinal
                )
            }

    }
}

@Composable
private fun buildBoatSelect(
    size: Int,
    maxBoatCellSize: Dp,
    state: BoatSelector = BoatSelector.isNotSelected,
    name: String = " ",
    modifier: Modifier = Modifier,
    onClick: (idx: Int) -> Unit = { _->},
    listIdx: Int = 0
){
    Box(Modifier.border(color = Color.Black, width = 1.dp)){
        drawCell(
            boarderColor = Color.Transparent,
            cellFillColor =
                if (state == BoatSelector.isNotSelected) Color.Green
                else if (state == BoatSelector.hasBeenPlaced) Color.Red
                else Color.Magenta,
            cellText = name,
            modifier = modifier.width(maxBoatCellSize*size),
            onClick = {
                onClick(listIdx)
            }
        )
    }

}

@Preview
@Composable
private fun buildBoatPreview(){
    buildBoatSelect(
        size = 3,
        name = "Cruiser",
        modifier = Modifier,
        maxBoatCellSize = 200.dp
    )
}

@Preview
@Composable
private fun selectFleetPreview(){
   FleetSelectorView()
}