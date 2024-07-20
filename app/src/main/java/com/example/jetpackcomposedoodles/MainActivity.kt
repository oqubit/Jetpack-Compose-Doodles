package com.example.jetpackcomposedoodles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposedoodles.doodles.HoneycombScreen
import com.example.jetpackcomposedoodles.ui.theme.JetpackComposeDoodlesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeDoodlesTheme {
                val navController = rememberNavController()
                NavigationAppHost(navController = navController)
            }
        }
    }
}

sealed class Destination(val route: String) {
    data object HomeScreen : Destination("home_screen")
    data object HoneycombScreen : Destination("honeycomb_screen")
}

@Composable
fun NavigationAppHost(navController: NavHostController) {
   NavHost(navController= navController, startDestination = "home_screen") {
       composable(Destination.HomeScreen.route) { HomeScreen(navController) }
       composable(Destination.HoneycombScreen.route) { HoneycombScreen() }
   }
}