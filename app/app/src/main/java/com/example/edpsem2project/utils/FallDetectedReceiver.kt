package com.example.edpsem2project.utils;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class FallDetectedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.edpsem2project.FALL_DETECTED") {
            Log.d("FallDetectedReceiver", "Fall detected broadcast received")
            // Add logic to handle the broadcast
        }
    }
}