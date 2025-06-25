package com.example.edpsem2project.primary_Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edpsem2project.R
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


@Composable
fun MainScreen(navController: NavController) {
    // Main screen content goes here
    var isUserAtHome by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (! isUserAtHome) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Get the last location and open map screen
                Text(
                    text = "User is away from Home",
                    fontSize = 34.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp // Adjust line height for better spacing
                )
                Image(
                    painter = painterResource(id = R.drawable.locate),
                    contentDescription = "Locate",
                    modifier = Modifier.size(400.dp)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF1976D2), // Blue color
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { navController.navigate("map_screen") }
                        .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Detect Location",
                        color = Color.White,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
        else{
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Get the last location and open map screen
                Text(
                    text = "User is at Home",
                    fontSize = 34.sp,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.at_home),
                    contentDescription = "Locate",
                    modifier = Modifier.size(400.dp)
                )
            }
        }

        // Toggle isUserAtHome state button
        Button(
            onClick = {
                isUserAtHome = !isUserAtHome
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Toggle User Location")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview(){
    val mockNavController = NavController(LocalContext.current)
    MainScreen(mockNavController)
}