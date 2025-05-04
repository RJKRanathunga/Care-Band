package com.example.edpsem2project.primary_Screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.utils.BluetoothViewModel
import java.io.IOException

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

//@Composable
//fun SettingsScreen(navController: NavController) {
//    val context = LocalContext.current
//    var isConnected by remember { mutableStateOf(false) }
//    var socket by remember { mutableStateOf<BluetoothSocket?>(null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Settings Screen",
//            modifier = Modifier.padding(16.dp)
//        )
//
//        Button(onClick = {
//            Log.d("Bluetooth1", "Connect button clicked1")
//            if (!isConnected) {
//                Log.d("Bluetooth1", "Connect button clicked")
//
//                Thread {
//                    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//                    Log.d("Bluetooth1", "Adapter found: $bluetoothAdapter")
//
//                    val device = if (ActivityCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.BLUETOOTH_CONNECT
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        Log.d("Bluetooth1", "Permission not granted for BLUETOOTH_CONNECT")
//                        return@Thread
//                    } else {
//                        Log.d("Bluetooth1", "Permission granted for BLUETOOTH_CONNECT")
//                        bluetoothAdapter?.bondedDevices?.find { it.name == "ESP32_BT_Device" }
//                    }
//
//                    if (device == null) {
//                        Log.d("Bluetooth1", "ESP32 device not found among bonded devices")
//                        return@Thread
//                    } else {
//                        Log.d("Bluetooth1", "Found bonded device: ${device.name}")
//                    }
//
//                    try {
//                        val uuid = device.uuids[0].uuid
//                        Log.d("Bluetooth1", "Using UUID: $uuid")
//
//                        val tmpSocket = device.createRfcommSocketToServiceRecord(uuid)
//                        Log.d("Bluetooth1", "Socket created, cancelling discovery...")
//                        bluetoothAdapter.cancelDiscovery()
//
//                        Log.d("Bluetooth1", "Trying to connect socket...")
//                        tmpSocket.connect()
//                        Log.d("Bluetooth1", "Socket connected successfully!")
//
//                        (context as android.app.Activity).runOnUiThread {
//                            socket = tmpSocket
//                            isConnected = true
//                        }
//                    } catch (e: IOException) {
//                        Log.e("Bluetooth1", "Socket connection failed", e)
//                    }
//                }.start()
//            } else {
//                Log.d("Bluetooth1", "Already connected")
//            }
//        },enabled = !isConnected) {
//            Text(if (isConnected) "Connected" else "Connect")
//        }
//
//        WifiInputPanel { ssid, password ->
//            // Handle the WiFi SSID and password here
//            val jsonData = """{"ssid": "$ssid", "password": "$password"}"""
//            Log.d("Bluetooth1", "Prepared JSON data: $jsonData")
//            Thread {
//                try {
//                    Log.d("Bluetooth1", "Sending JSON data: $jsonData")
//                    socket?.outputStream?.write(jsonData.toByteArray())
//                    Log.d("Bluetooth1", "JSON data sent")
//                } catch (e: IOException) {
//                    Log.e("Bluetooth1", "Failed to send JSON data", e)
//                }
//            }.start()
//        }
//    }
//}

@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: BluetoothViewModel = viewModel(LocalContext.current as ComponentActivity)
    val isConnected by viewModel.isConnected

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

        Button(onClick = {
            Log.d("Bluetooth1", "Connect button clicked1")
            viewModel.connectToBluetooth(context)
        },enabled = !isConnected) {
            Text(if (isConnected) "Connected" else "Connect")
        }

        WifiInputPanel { ssid, password ->
            // Handle the WiFi SSID and password here
            val jsonData = """{"ssid": "$ssid", "password": "$password"}"""
            Log.d("Bluetooth1", "Prepared JSON data: $jsonData")
            viewModel.sendData(jsonData)
        }
    }
}

//@Preview
//@Composable
//fun SettingsScreenPreview() {
//    val mockNavController = NavController(LocalContext.current)
//    SettingsScreen(mockNavController)
//}