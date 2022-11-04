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
){
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxHeight()){
            for (i in 1..10){
                drawRowOfColumns()
            }
        }
    }
}

@Composable
private fun drawRowOfColumns(){
    Row(modifier = Modifier
        .background(color = Color.LightGray)
    ){
        for (k in 1.. 10){
            Box(modifier = Modifier
                .border(width = 1.dp, color = Color.Black)
                .width(width = 18.dp)){
                Text(" ")
            }
        }
    }
}




@Preview
@Composable
fun BoardViewPreview(){
    BoardView()
}


/*Canvas(modifier = Modifier.fillMaxSize().clickable {  }){
           val canvasSize = size
           val canvasWidth = size.width
           val canvasHeight = size.height

           val smallestOrientation =
               if (canvasHeight < canvasWidth) canvasHeight / 12
               else canvasWidth / 12


           for (i in 1..10){
               for (k in 1..10){
                   drawRect(
                       color = if ( (i+k) % 2 == 0) Color.Red else Color.Blue,
                       topLeft = Offset(x = smallestOrientation * i, y = smallestOrientation * k),
                       size = Size(width = smallestOrientation, height = smallestOrientation)
                   )
               }
           }
       }*/