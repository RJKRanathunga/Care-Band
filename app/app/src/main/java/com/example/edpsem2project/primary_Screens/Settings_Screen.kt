package com.example.edpsem2project.primary_Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.R
import com.example.edpsem2project.utils.BluetoothViewModel

@Composable
fun ConnectButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
    buttonText: String = "Connect",
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (enabled) Color(0xFF1976D2) else Color.Gray,
                shape = RoundedCornerShape(10.dp)
            )
            .then(
                if (enabled) Modifier.clickable { onClick() } else Modifier
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: BluetoothViewModel = viewModel()
    val isConnected by viewModel.isConnected
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding around the border
                .border(
                    width = 3.dp,
                    color = Color(0xFF1976D2),
                    shape = RoundedCornerShape(16.dp) // Rounded border
                )
                .padding(16.dp), // Padding inside the border
            horizontalAlignment = Alignment.CenterHorizontally
        ){ // Connect to bluetooth
            Row() {
                Image(
                    painter = painterResource(R.drawable.bluetooth),
                    contentDescription = "bluetooth",
                    modifier = Modifier.size(width = 70.dp, height = 70.dp)
                )
                Text(
                    text = "CONNECT VIA BLUETOOTH",
                    color = Color.White
                )
            }
            Row(){
                ConnectButton(
                    enabled = ! isConnected,
                    buttonText = "Connect",
                    onClick = {
                        Log.d("Bluetooth1", "Connect button clicked1")
                        viewModel.connectToBluetooth(context)
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                ConnectButton(
                    enabled = isConnected,
                    buttonText = "Disconnect",
                    onClick = {
                        viewModel.disConnect()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
//**********************************************************************************************
        WifiInputPanel(
            enabled = viewModel.isConnected.value,
            onSubmit = { ssid, password ->
                // Handle the WiFi SSID and password here
                val jsonData = """{"ssid": "$ssid", "password": "$password" ,"command": "connect_wifi"}"""
                Log.d("Bluetooth1", "Prepared JSON data: $jsonData")
                viewModel.sendData(jsonData)
            }
        )
//**********************************************************************************************
        Column( // Define home box
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding around the border
                .border(
                    width = 3.dp,
                    color = Color(0xFF1976D2),
                    shape = RoundedCornerShape(16.dp) // Rounded border
                )
                .padding(16.dp), // Padding inside the border
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.define_home),
                contentDescription = "My Home"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = if (viewModel.isConnected.value) Color(0xFF1976D2) else Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .then(
                        if (viewModel.isConnected.value) Modifier.clickable { viewModel.sendData("""{"command": "confirm_home"}""") } else Modifier
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Set Current Location as Home",
                    color = Color.White,
                    textAlign = TextAlign.Center

                )
            }
        }
        //**********************************************************************************************
        Column( // Reset memory in the device
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding around the border
                .border(
                    width = 3.dp,
                    color = Color(0xFFb04537),
                    shape = RoundedCornerShape(16.dp) // Rounded border
                )
                .padding(16.dp), // Padding inside the border
            horizontalAlignment = Alignment.CenterHorizontally
        ){ // Connect to bluetooth
            Row() {
                Image(
                    painter = painterResource(R.drawable.reset),
                    contentDescription = "bluetooth",
                    modifier = Modifier.size(width = 90.dp, height = 90.dp)
                )
                Text(
                    text = "RESET MEMORY",
                    color = Color.Red
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = if (viewModel.isConnected.value) Color(0xFF1976D2) else Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .then(
                        if (viewModel.isConnected.value) Modifier.clickable { viewModel.sendData("""{"command": "clear_memory"}""") } else Modifier
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RESET",
                    color = Color(0xFFb04537),
                    textAlign = TextAlign.Center

                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    val mockNavController = NavController(LocalContext.current)
    SettingsScreen(mockNavController)
}

@Composable
fun WifiInputPanel(enabled:Boolean, onSubmit: (String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Button to show the dialog
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Padding around the border
            .border(
                width = 3.dp,
                color = Color(0xFF1976D2),
                shape = RoundedCornerShape(16.dp) // Rounded border
            )
            .padding(16.dp), // Padding inside the border
        horizontalAlignment = Alignment.CenterHorizontally
    ){ // Connect to bluetooth
        Row() {
            Image(
                painter = painterResource(R.drawable.wifi),
                contentDescription = "bluetooth",
                modifier = Modifier.size(width = 70.dp, height = 70.dp)
            )
            Text(
                text = "CONNECT TO HOME WIFI",
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (enabled) Color(0xFF1976D2) else Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )
                .then(
                    if (enabled) Modifier.clickable { showDialog=true } else Modifier
                )
                .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Enter WiFi Details",
                color = Color.White,
                textAlign = TextAlign.Center

            )
        }
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