package isel.pdm.ui.elements

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.ui.elements.buttons.BiState
import isel.pdm.utils.drawCell

enum class Fleet(val size: Int, val color: Color){
    Destroyer(2, Color.Blue),
    Submarine(3, Color.Cyan),
    Cruiser(3, Color.Magenta),
    BattleShip( 4, Color.Yellow),
    Carrier(5, Color.Green)
}

@Composable
fun FleetView(
    modifier: Modifier = Modifier,
    onClick: (idx: Int) -> Unit = { _->},
    selectedBoatStateList: List<BiState> = List(Fleet.values().size){_ -> BiState.hasNotBeenPressed}
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
                buildBoat(size = Fleet.Destroyer.size, name = Fleet.Destroyer.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[Fleet.Destroyer.ordinal],
                    listIdx = Fleet.Destroyer.ordinal
                )
                Spacer(modifier = spaceModifier)
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    buildBoat(size = Fleet.Submarine.size, name = Fleet.Submarine.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = selectedBoatStateList[Fleet.Submarine.ordinal],
                        listIdx = Fleet.Submarine.ordinal
                    )
                    Spacer(modifier = spaceModifier)
                    buildBoat(size = Fleet.Cruiser.size, name = Fleet.Cruiser.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = selectedBoatStateList[Fleet.Cruiser.ordinal],
                        listIdx = Fleet.Cruiser.ordinal
                    )
                }
                Spacer(modifier = spaceModifier)
                buildBoat(size= Fleet.BattleShip.size, name = Fleet.BattleShip.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[Fleet.BattleShip.ordinal],
                    listIdx = Fleet.BattleShip.ordinal
                )
                Spacer(modifier = spaceModifier)
                buildBoat(size = Fleet.Carrier.size, name = Fleet.Carrier.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = selectedBoatStateList[Fleet.Carrier.ordinal],
                    listIdx = Fleet.Carrier.ordinal
                )
            }

    }
}

@Composable
private fun buildBoat(
    size: Int,
    maxBoatCellSize: Dp,
    color: Color = Color.Green,
    state: BiState = BiState.hasNotBeenPressed,
    name: String = " ",
    modifier: Modifier = Modifier,
    onClick: (idx: Int) -> Unit = { _->},
    listIdx: Int = 0
){
    Box(Modifier.border(color = Color.Black, width = 1.dp)){
        drawCell(
            boarderColor = Color.Transparent,
            cellFillColor = if (state == BiState.hasNotBeenPressed) color else Color.Red,
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
    buildBoat(
        size = 3,
        color = Color.Green,
        name = "Cruiser",
        modifier = Modifier,
        maxBoatCellSize = 200.dp
    )
}

@Preview
@Composable
private fun selectFleetPreview(){
   FleetView()
}