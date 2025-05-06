package com.example.edpsem2project.primary_Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.REDIS_PASSWORD
import com.example.edpsem2project.REDIS_USERNAME
import com.example.edpsem2project.utils.BluetoothViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

data class Location(val latitude: Double, val longitude: Double)
data class RedisWrapper(val result: List<String>)


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


@SuppressLint("ContextCastToActivity")
@Composable
fun MainScreen(navController: NavController) {
    // Main screen content goes here
    val context = LocalContext.current
    val viewModel: BluetoothViewModel = viewModel(LocalContext.current as ComponentActivity)
    val isConnected by viewModel.isConnected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Connect to Bluetooth
        Button(onClick = {
            Log.d("Bluetooth1", "Connect button clicked1")
            viewModel.connectToBluetooth(context)
        },enabled = !isConnected) {
            Text(if (isConnected) "Connected" else "Connect")
        }
        Button(onClick = {
            viewModel.disConnect()
        },enabled = isConnected) {
            Text("Disconnect")
        }

        // Get the last location and open map screen
        Text("Get the last location")
        Button(
            onClick = {navController.navigate("map_screen")}
        ) {
            Text(text = "Detect Location")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview(){
    val mockNavController = NavController(LocalContext.current)
    MainScreen(mockNavController)
}