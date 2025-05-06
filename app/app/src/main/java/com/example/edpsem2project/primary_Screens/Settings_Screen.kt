package com.example.edpsem2project.primary_Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.utils.BluetoothViewModel

@Composable
fun WifiInputPanel(onSubmit: (String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Button to show the dialog
    Button(onClick = { showDialog = true }) {
        Text("Enter WiFi Details")
    }

    // Dialog for input
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter WiFi Details") },
            text = {
                Column {
                    TextField(
                        value = ssid,
                        onValueChange = { ssid = it },
                        label = { Text("WiFi SSID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("WiFi Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onSubmit(ssid, password) // Trigger the function with input values
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: BluetoothViewModel = viewModel(LocalContext.current as ComponentActivity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings Screen",
            modifier = Modifier.padding(16.dp)
        )

        WifiInputPanel { ssid, password ->
            // Handle the WiFi SSID and password here
            val jsonData = """{"ssid": "$ssid", "password": "$password" ,"command": "connect_wifi"}"""
            Log.d("Bluetooth1", "Prepared JSON data: $jsonData")
            viewModel.sendData(jsonData)
        }
        Button(
            onClick = {
                viewModel.sendData("""{"command": "confirm_home"}""")
            }
        ){
            Text("Confirm Home")
        }

        Button(
            onClick = {
                viewModel.sendData("""{"command": "clear_memory"}""")
            }
        ){
            Text("Reset memory")
        }
        Button(
            onClick = {
                viewModel.sendData("""{"command": "shut_down"}""")
            }
        ){
            Text("Shut down")
        }
        Button(
            onClick = {
                viewModel.sendData("""{"command": "restart"}""")
            }
        ){
            Text("Restart")
        }
        Button(
            onClick = {
                viewModel.sendData("""{"command": "connect_saved_wifi"}""")
            }
        ){
            Text("Connect saved WiFi")
        }
        Button(
            onClick = {
                viewModel.sendData("""{"command": "potential_zone_left"}""")
            }
        ) {
            Text(text = "Potential Zone Left")
        }
    }
}

//@Preview
//@Composable
//fun SettingsScreenPreview() {
//    val mockNavController = NavController(LocalContext.current)
//    SettingsScreen(mockNavController)
//}