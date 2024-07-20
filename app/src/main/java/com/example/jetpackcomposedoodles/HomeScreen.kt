package com.example.jetpackcomposedoodles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            ) {
                Text(
                    text = "Home Screen",
                    style = TextStyle(
                        brush = Brush.linearGradient(listOf(Color(0xFF0F6FFF), Color(0xFF77FFA9))),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 50.sp
                    ),
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
            Button(onClick = { navController.navigate(Destination.HoneycombScreen.route) }) {
                Text(text = "Honeycomb Screen")
            }
            Button(onClick = { navController.navigate(Destination.CircularHoneycombScreen.route) }) {
                Text(text = "Circular Honeycomb Screen")
            }
        }
    }
}