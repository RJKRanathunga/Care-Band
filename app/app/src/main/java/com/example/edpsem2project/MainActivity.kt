package com.example.edpsem2project

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edpsem2project.ui.theme.EDPSem2ProjectTheme
import com.example.edpsem2project.primary_Screens.MainScreen
import com.example.edpsem2project.primary_Screens.SettingsScreen
import com.example.edpsem2project.secondary_Screens.GoogleMapScreen
import android.Manifest
import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.edpsem2project.utils.BluetoothViewModel

class MainActivity : ComponentActivity() {
//    private lateinit var bluetoothPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
    private lateinit var multiplePermissionsLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request Bluetooth permissions
        multiplePermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val connectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] ?: false
            val scanGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] ?: false

            if (connectGranted) {
                Log.d("Bluetooth1", "BLUETOOTH_CONNECT granted")
            } else {
                Log.d("Bluetooth1", "BLUETOOTH_CONNECT denied")
            }

            if (scanGranted) {
                Log.d("Bluetooth1", "BLUETOOTH_SCAN granted")
            } else {
                Log.d("Bluetooth1", "BLUETOOTH_SCAN denied")
            }
        }
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (permissionsToRequest.isNotEmpty()) {
            multiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }



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