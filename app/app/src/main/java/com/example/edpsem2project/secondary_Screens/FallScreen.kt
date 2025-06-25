package com.example.edpsem2project.secondary_Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.edpsem2project.R

@Composable
fun FallDetectedOverlay(onDismiss: () -> Unit, onDetectLocation: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fall Detected",
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(id = R.drawable.fall),
            contentDescription = "Fall Detected",
            modifier = Modifier.size(400.dp)
        )
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF1976D2), // Blue color
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onDetectLocation() }
                .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Detect Location",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = { onDismiss() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Dismiss")
        }
    }
}