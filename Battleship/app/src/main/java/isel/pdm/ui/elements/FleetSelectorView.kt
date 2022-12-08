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
import isel.pdm.data.game.TypeOfShip
import isel.pdm.utils.drawCell


enum class ShipState {hasBeenPlaced, isSelected, isNotSelected}

@Composable
fun FleetSelectorView(
    modifier: Modifier = Modifier,
    onClick: (boatSelected: TypeOfShip) -> Unit = { _->},
    shipState: Map<TypeOfShip, ShipState> = TypeOfShip.values().associateWith { _ -> ShipState.isNotSelected }
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
                    size = TypeOfShip.Destroyer.size, name = TypeOfShip.Destroyer.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = shipState[TypeOfShip.Destroyer]!!,
                    boatSelected = shipState.keys.first { e -> e == TypeOfShip.Destroyer }
                )
                Spacer(modifier = spaceModifier)
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    buildBoatSelect(
                        size = TypeOfShip.Submarine.size, name = TypeOfShip.Submarine.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = shipState[TypeOfShip.Submarine]!!,
                        boatSelected = shipState.keys.first { e -> e == TypeOfShip.Submarine }
                    )
                    Spacer(modifier = spaceModifier)
                    buildBoatSelect(
                        size = TypeOfShip.Cruiser.size, name = TypeOfShip.Cruiser.name,
                        maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                        onClick = onClick, state = shipState[TypeOfShip.Cruiser]!!,
                        boatSelected = shipState.keys.first { e -> e == TypeOfShip.Cruiser }
                    )
                }
                Spacer(modifier = spaceModifier)
                buildBoatSelect(
                    size = TypeOfShip.BattleShip.size, name = TypeOfShip.BattleShip.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = shipState[TypeOfShip.BattleShip]!!,
                    boatSelected = shipState.keys.first { e -> e == TypeOfShip.BattleShip }
                )
                Spacer(modifier = spaceModifier)
                buildBoatSelect(
                    size = TypeOfShip.Carrier.size, name = TypeOfShip.Carrier.name,
                    maxBoatCellSize = maxBoatCellsSize, modifier = modifier,
                    onClick = onClick, state = shipState[TypeOfShip.Carrier]!!,
                    boatSelected = shipState.keys.first { e -> e == TypeOfShip.Carrier }
                )
            }

    }
}

@Composable
private fun buildBoatSelect(
    size: Int,
    maxBoatCellSize: Dp,
    state: ShipState = ShipState.isNotSelected,
    name: String = " ",
    modifier: Modifier = Modifier,
    onClick: (boatSelected: TypeOfShip) -> Unit = { _->},
    boatSelected: TypeOfShip
){
    Box(Modifier.border(color = Color.Black, width = 1.dp)){
        drawCell(
            boarderColor = Color.Transparent,
            cellFillColor = when(state){
                ShipState.isNotSelected -> Color.Green
                ShipState.hasBeenPlaced -> Color.Red
                ShipState.isSelected -> Color.Magenta
            },
            cellText = name,
            modifier = modifier.width(maxBoatCellSize*size),
            onClick = {
                onClick(boatSelected)
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
        maxBoatCellSize = 200.dp,
        boatSelected = TypeOfShip.Cruiser
    )
}

@Preview
@Composable
private fun selectFleetPreview(){
   FleetSelectorView()
}