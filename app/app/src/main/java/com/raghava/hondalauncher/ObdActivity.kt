package com.raghava.hondalauncher

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class ObdActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val bluetoothManager =
                getSystemService(BluetoothManager::class.java)

            val bluetoothAdapter =
                bluetoothManager.adapter

            val pairedDevices: Set<BluetoothDevice> =
                if (
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.bondedDevices ?: emptySet()
                } else {
                    emptySet()
                }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Text(
                    text = "🚗 OBD Dashboard",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Text(
                    text = "Paired Devices",
                    color = Color.Yellow
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                pairedDevices.forEach { device ->

                    Text(
                        text = device.name ?: "Unknown Device",
                        color = Color.White
                    )
                }

                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Text(
                    text = "Speed : 0 km/h",
                    color = Color.White
                )

                Text(
                    text = "RPM : 0",
                    color = Color.White
                )

                Text(
                    text = "Fuel : 80%",
                    color = Color.White
                )
                }
            }
        }
    }