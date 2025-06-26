package com.example.edpsem2project.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.edpsem2project.MainActivity
import com.example.edpsem2project.OverlayActivity
import com.example.edpsem2project.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

fun sendTokenToServer(token: String) {
    val url = "https://edpsemester2.onrender.com"

    val json = """
        {
            "token": "$token",
            "command": "save_token"
        }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .header("Content-Type", "application/json")
        .header("my-api-key","hsdjsiwkqoo1k2o1llso0ssldhfuw9eikmf")
        .post(requestBody)
        .build()

    OkHttpClient().newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FCM", "Failed to add token: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("FCM", "Token added: ${response.body?.string()}")
        }
    })

}

// Fetch the Firebase token
fun getFirebaseToken() {
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            if (!task.isSuccessful) {
//                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get the FCM token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")

            // Send the token to your backend
            sendTokenToServer(token)
        }
}

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val message = remoteMessage.data["message"]
        Log.d("FCM", "Message received: $remoteMessage")
        Log.d("FCM", "Message received: ${remoteMessage.data}")
        Log.d("FCM", "Message received: ${remoteMessage.data["message"]}")
        createNotificationChannel()
        val intent = Intent(this, OverlayActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("message", message)
        }
        startActivity(intent)
//        when (message) {
//            "fall_detected" -> {
//                val intent = Intent(this, OverlayActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    putExtra("message", message)
//                }
//                startActivity(intent)
//            }
//            "absolute_house_left" -> {
//
//            }
//            "possible_house_left" -> {
//
//            }
//        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "default_channel",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for default notifications"
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Send the token to your backend for future messaging
        getFirebaseToken()
    }

}
