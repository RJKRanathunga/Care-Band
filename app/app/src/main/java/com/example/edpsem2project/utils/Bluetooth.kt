package com.example.edpsem2project.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import java.io.IOException

class BluetoothViewModel : ViewModel() {
    private val _isConnected = mutableStateOf(false)
    val isConnected = _isConnected

    private var _socket: BluetoothSocket? = null

    fun connectToBluetooth(context: Context) {
        if (_isConnected.value) return

        Thread {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            Log.d("Bluetooth1", "Adapter found: $bluetoothAdapter")

            val device = if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("Bluetooth1", "Permission not granted for BLUETOOTH_CONNECT")
                return@Thread
            } else {
                Log.d("Bluetooth1", "Permission granted for BLUETOOTH_CONNECT")
                bluetoothAdapter?.bondedDevices?.find { it.name == "ESP32_BT_Device" }
            }

            if (device == null) {
                Log.d("Bluetooth1", "ESP32 device not found among bonded devices")
                return@Thread
            } else {
                Log.d("Bluetooth1", "Found bonded device: ${device.name}")
            }
            try {
                val uuid = device.uuids[0].uuid
                Log.d("Bluetooth1", "Using UUID: $uuid")

                val tmpSocket = device.createRfcommSocketToServiceRecord(uuid)
                Log.d("Bluetooth1", "Socket created, cancelling discovery...")
                bluetoothAdapter.cancelDiscovery()

                Log.d("Bluetooth1", "Trying to connect socket...")
                tmpSocket.connect()
                Log.d("Bluetooth1", "Socket connected successfully!")


                _socket = tmpSocket
                _isConnected.value = true
            } catch (e: IOException) {
                Log.e("Bluetooth", "Connection failed", e)
            }
        }.start()
    }

    fun sendData(json: String) {
        try {
            Log.d("Bluetooth1", "Sending JSON data: $json")
            _socket?.outputStream?.write(json.toByteArray())
            Log.d("Bluetooth1", "JSON data sent")
        } catch (e: IOException) {
            Log.e("Bluetooth", "Send failed", e)
        }
    }

    fun disConnect(){
        _isConnected.value = false
    }
}