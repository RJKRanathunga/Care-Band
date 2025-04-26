package com.example.edpsem2project.secondary_Screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

fun openGoogleMapsNavigation(context: Context, destinationLat: Double, destinationLng: Double) {
    val gmmIntentUri = Uri.parse("google.navigation:q=$destinationLat,$destinationLng&mode=d")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapScreen() {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    val location = LatLng(6.7962, 79.9016) // Example: University of Moratuwa


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(6.7962, 79.9016), // Optional fallback starting point
                    15f
                )
            )
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column( modifier = Modifier.fillMaxSize() ) {
        Box( modifier = Modifier.weight(1f) ){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionState.status.isGranted
                )
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = "University of Moratuwa",
                    snippet = "Here we are!"
                )
            }
        }
        Button(
            onClick = {
                openGoogleMapsNavigation(context, location.latitude, location.longitude)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Navigate to Target")
        }
    }
}