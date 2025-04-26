package com.example.edpsem2project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edpsem2project.ui.theme.EDPSem2ProjectTheme
import com.example.edpsem2project.primary_Screens.MainScreen
import com.example.edpsem2project.primary_Screens.SettingsScreen
import com.example.edpsem2project.secondary_Screens.GoogleMapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EDPSem2ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
                    ProjectMain(modifier = Modifier.padding(contentPadding))
                }
            }
        }
    }
}

@Composable
fun ProjectMain(modifier: Modifier) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf("Main", "Settings"),
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main_screen") { MainScreen(navController) }
            composable("settings_screen") { SettingsScreen(navController) }
            composable("map_screen") { GoogleMapScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<String>,
    navController: NavController
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val icon = when (item){
                "Main" -> painterResource(R.drawable.home)
                "Settings" -> painterResource(R.drawable.settings)
                else -> painterResource(R.drawable.ic_launcher_foreground)
            }
            NavigationBarItem(
                selected = currentRoute == item.toRoute(),
                onClick = {
                    navController.navigate(item.toRoute()) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false } // Ensures navigation to the main screen
                        launchSingleTop = true // Avoids duplicate instances
                    }
                },
                label = { Text(item) },
                icon = { Icon(painter = icon, contentDescription = null, modifier = Modifier.size(24.dp)) }
            )
        }
    }
}

// Extension function to map the item names to routes
fun String.toRoute(): String {
    return when (this) {
        "Main" -> "main_screen"
        "Settings" -> "settings_screen"
        else -> "main_screen"
    }
}