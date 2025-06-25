package com.example.edpsem2project.primary_Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.utils.BluetoothViewModel

@Composable
fun DeveloperScreen(navController: NavController){
    val viewModel: BluetoothViewModel = viewModel()

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

        WifiInputPanel (
            enabled = true,
            onSubmit = {ssid, password ->
            // Handle the WiFi SSID and password here
            val jsonData = """{"ssid": "$ssid", "password": "$password" ,"command": "connect_wifi"}"""
            Log.d("Bluetooth1", "Prepared JSON data: $jsonData")
            viewModel.sendData(jsonData)
            }
        )
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