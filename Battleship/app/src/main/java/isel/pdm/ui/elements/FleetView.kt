package isel.pdm.ui.elements

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.pdm.utils.drawCell

@Composable
fun FleetView(
    modifier: Modifier = Modifier,
){
    BoxWithConstraints(modifier = modifier.padding(4.dp)) {
        val maxBoatCellsSize = this.maxWidth / 6
        val spaceModifier = Modifier.height(4.dp).width(4.dp)
        Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                buildBoat(size = 2, color = Color.Magenta, name = "Destroyer",
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = {
                        Log.v("GAME_PREP", "BoatWasClicked")
                    }
                )
                Spacer(modifier = spaceModifier)
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    buildBoat(size = 3, color = Color.Red, name = "Submarine",
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = {
                            Log.v("GAME_PREP", "BoatWasClicked")
                        }
                    )
                    Spacer(modifier = spaceModifier)
                    buildBoat(size = 3, color = Color.Cyan, name = "Cruiser",
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = {
                            Log.v("GAME_PREP", "BoatWasClicked")
                        }
                    )
                }
                Spacer(modifier = spaceModifier)
                buildBoat(size= 4, color = Color.Yellow, name = "BattleShip",
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = {
                        Log.v("GAME_PREP", "BoatWasClicked")
                    }
                )
                Spacer(modifier = spaceModifier)
                buildBoat(size = 5, color = Color.Green, name = "Carrier",
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = {
                        Log.v("GAME_PREP", "BoatWasClicked")
                    }
                )
            }

    }
}

@Composable
private fun buildBoat(
    size: Int,
    maxBoatCellSize: Dp,
    color: Color,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    drawCell(
        boarderColor = Color.Transparent,
        cellFillColor = color,
        cellText = name,
        modifier = modifier.width(maxBoatCellSize*size),
        onClick = onClick
    )
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
private fun buildFleetPreview(){
   FleetView()
}