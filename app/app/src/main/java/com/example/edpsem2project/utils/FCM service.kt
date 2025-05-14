package com.example.edpsem2project.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

fun sendTokenToServer(token: String) {
    val url = "https://model-quagga-28525.upstash.io/sadd/fcm_tokens/$token"

    val request = Request.Builder()
        .url(url)
        .header("Authorization", "Bearer AW9tAAIncDE4NGQ3NDY3YjM2NjY0OTEzYTEwNzYwZWM4YjgxZjA0YnAxMjg1MjU")
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
        val type = remoteMessage.data["type"]
        Log.d("FCM", "Message received: $remoteMessage")
        Log.d("FCM", "Message received: ${remoteMessage.data}")
        Log.d("FCM", "Message received: ${remoteMessage.data["body"]}")
        when (type) {
            "Pomodoro" -> {

            }
            "Short break" -> {

            }
            "Long break" -> {

            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Send the token to your backend for future messaging
        getFirebaseToken()
    }

}
