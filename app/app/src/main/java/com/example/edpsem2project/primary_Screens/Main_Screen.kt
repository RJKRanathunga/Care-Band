package com.example.edpsem2project.primary_Screens

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.edpsem2project.REDIS_PASSWORD
import com.example.edpsem2project.REDIS_USERNAME
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

data class Location(val latitude: Double, val longitude: Double)
data class RedisWrapper(val result: List<String>)


//@Composable
//fun BluetoothSenderUI() {
//    val context = LocalContext.current
//    var isConnected by remember { mutableStateOf(false) }
//    var socket by remember { mutableStateOf<BluetoothSocket?>(null) }
//    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.BLUETOOTH_SCAN), 1)
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
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
//                        socket = tmpSocket
//                        isConnected = true
//                    } catch (e: IOException) {
//                        Log.e("Bluetooth1", "Socket connection failed", e)
//                    }
//                }.start()
//            } else {
//                Log.d("Bluetooth1", "Already connected")
//            }
//        }, enabled = ! isConnected) {
//            Text(if (isConnected) "Connected" else "Connect")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            Log.d("Bluetooth1", "Send button clicked")
//
//            Thread {
//                try {
//                    val message = "Hello ESP32\n"
//                    Log.d("Bluetooth1", "Sending message: $message")
//                    socket?.outputStream?.write(message.toByteArray())
//                    Log.d("Bluetooth1", "Message sent")
//                } catch (e: IOException) {
//                    Log.e("Bluetooth1", "Failed to send message", e)
//                }
//            }.start()
//        }, enabled = isConnected) {
//            Text("Send Message")
//        }
//    }
//}



fun getLastLocation(onResult: (List<Location>) -> Unit) {
    val client = OkHttpClient()
    val url = "https://${REDIS_USERNAME}/lrange/location/0/0"

    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $REDIS_PASSWORD")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("redis", "Failed to fetch value: ${e.message}")
            onResult(emptyList()) // send empty list on failure
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    Log.d("redis", "Unexpected response: ${response.code}")
                    onResult(emptyList())
                } else {
                    val body = response.body?.string()
                    try {
                        val gson = Gson()
                        val wrapper = gson.fromJson(body, RedisWrapper::class.java)

                        if (wrapper.result.isNotEmpty()) {
                            val item = wrapper.result[0] // get the only string: "(6.7962, 79.9016)"
                            val cleaned = item.removeSurrounding("(", ")")
                            val parts = cleaned.split(",")
                            val lat = parts[0].trim().toDouble()
                            val lon = parts[1].trim().toDouble()
                            val location = Location(lat, lon)

                            onResult(listOf(location)) // Return a list with one Location object
                        } else {
                            onResult(emptyList())
                        }
                    } catch (e: Exception) {
                        Log.e("redis", "Failed to parse JSON: ${e.message}")
                        onResult(emptyList())
                    }
                }
            }
        }
    })
}


@Composable
fun MainScreen(navController: NavController) {
    // Main screen content goes here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Get the last location")
        Button(
            onClick = {navController.navigate("map_screen")}
        ) {
            Text(text = "Open Maps")
        }
        Button(
            onClick = {
//                val lastLocation = getLastLocation()
//                Log.d("redis", "Last location: $lastLocation")
                getLastLocation { locations ->
                    if (locations.isNotEmpty()) {
                        val loc = locations[0]
                        Log.d("redis", "Fetched location: ${loc.latitude}, ${loc.longitude}")
                    } else {
                        Log.d("redis", "No location found.")
                    }
                }
            }
        ) {
            Text(text = "Get Last Location")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview(){
    val mockNavController = NavController(LocalContext.current)
    MainScreen(mockNavController)
}