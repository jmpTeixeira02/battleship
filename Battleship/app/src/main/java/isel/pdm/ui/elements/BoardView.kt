package isel.pdm.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.data.PlayerMatchmaking


@Composable
fun BoardView(
    modifier: Modifier = Modifier
){
    BoxWithConstraints(modifier = modifier){
        val boxWithConstraintsScope = this
        Column()
        {
            for (i in 1..10){
                drawRowOfColumns(boxWithConstraintsScope)
            }
        }
    }
}

@Composable
private fun drawRowOfColumns(boxWithConstraintsScope: BoxWithConstraintsScope){
    Row(
        modifier = Modifier
            .background(color = Color.LightGray),
        horizontalArrangement = Arrangement.Start
    ){
        for (k in 1.. 10){
            Box(modifier = Modifier
                .width(boxWithConstraintsScope.maxWidth / 10)
                .height(boxWithConstraintsScope.maxHeight / 10)
                .border(width = 1.dp, color = Color.Black)
            ){
                Text(" ")
            }
        }
    }
}




@Preview
@Composable
fun BoardViewPreview(){
    BoardView(modifier = Modifier)
}