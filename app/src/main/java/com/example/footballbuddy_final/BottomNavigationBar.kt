package com.example.footballbuddy_final

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    onMatchesClick: () -> Unit,
    onNewsClick: () -> Unit,
    onStatisticsClick:  () -> Unit,

    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            color = Color.White,
            contentColor = Color.Black,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onMatchesClick) {
                    CustomIcon(
                        painter = painterResource(id = R.drawable.custom_soccer),
                        contentDescription = "Home",
                        onClick = onMatchesClick
                    )
                }
                IconButton(onClick = onNewsClick) {
                    CustomIcon(
                        painter = painterResource(id = R.drawable.custom_newspaper),
                        contentDescription = "Home",
                        onClick = onNewsClick
                    )
                }
                IconButton(onClick = onStatisticsClick) {
                    CustomIcon(
                        painter = painterResource(id = R.drawable.custom_trophy),
                        contentDescription = "Home",
                        onClick = onStatisticsClick
                    )
                }
            }
        }
    }
}
@Composable
fun CustomIcon(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit
) {
    androidx.compose.foundation.Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .width(58.dp) // Adjust the width as needed
            .height(58.dp) // Adjust the height as needed
            .clickable(onClick = onClick)
            .padding(8.dp) // Add padding for touch area
    )
}
