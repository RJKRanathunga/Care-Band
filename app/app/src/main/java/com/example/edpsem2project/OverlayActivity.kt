package com.example.edpsem2project

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class OverlayActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        // Play the sound
        mediaPlayer = MediaPlayer.create(this, R.raw.fall_sound).apply {
            isLooping = true  // Loop the sound continuously
            start()
        }

        super.onCreate(savedInstanceState)
        // Retrieve the message from the intent
        val message = intent.getStringExtra("message") ?: "No message"
        setContent {
            MaterialTheme {
                // Use insets-aware Scaffold
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                ) { contentPadding ->

                    when (message) {
                        "fall_detected" -> {
                            WarningDetectedOverlay(modifier = Modifier.padding(contentPadding),onDismiss = {
                                // Stop sound and finish activity when dismiss button pressed
                                safelyReleaseMediaPlayer()
                                finish()
                            }
                                ,title = "Fall Detected",
                                imageResId = R.drawable.fall
                            )
                        }
                        "absolute_house_left" -> {
                            WarningDetectedOverlay(modifier = Modifier.padding(contentPadding),onDismiss = {
                                // Stop sound and finish activity when dismiss button pressed
                                safelyReleaseMediaPlayer()
                                finish()
                            }
                                ,title = "User left the House",
                                imageResId = R.drawable.lost
                            )
                        }
                        "possible_house_left" -> {
                            WarningDetectedOverlay(modifier = Modifier.padding(contentPadding),onDismiss = {
                                // Stop sound and finish activity when dismiss button pressed
                                safelyReleaseMediaPlayer()
                                finish()
                            }
                                ,title = "User might have left the House",
                                imageResId = R.drawable.possible_lost
                            )
                        }
                    }
                                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        safelyReleaseMediaPlayer()
    }
    private fun safelyReleaseMediaPlayer() {
        if (::mediaPlayer.isInitialized) {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.release()
            } catch (e: IllegalStateException) {
                e.printStackTrace() // Log the error for debugging
            }
        }
    }
}

@Composable
fun WarningDetectedOverlay(
    modifier: Modifier, onDismiss: () -> Unit,title:String,
    imageResId:Int
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 34.sp,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Fall Detected",
            modifier = Modifier.size(400.dp)
        )
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFa67e0f), // Blue color
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    onDismiss()
                }
                .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Dismiss",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}