package com.raghava.hondalauncher

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import android.bluetooth.BluetoothAdapter

import androidx.compose.foundation.shape.RoundedCornerShape

import com.raghava.hondalauncher.weather.RetrofitClient

import androidx.core.net.toUri



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HondaLauncherApp()
        }
    }
}

@Composable
fun HondaLauncherApp() {

    var showSplash by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
    }

    if (showSplash) {
        HondaSplash()
    } else {
        HondaDashboard()
    }
}

@Composable
fun HondaSplash() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.honda_logo
            ),
            contentDescription = "Honda Logo"
        )

    }
}

@Composable
fun HondaDashboard() {

    val context = LocalContext.current

    var batteryPercent by remember {
        mutableStateOf(
            getBatteryPercentage(context)
        )
    }

    LaunchedEffect(Unit) {
        while (true) {

            batteryPercent =
                getBatteryPercentage(context)

            delay(30000)
        }
    }

    var wifiConnected by remember {
        mutableStateOf(
            isWifiConnected(context)
        )
    }

    var bluetoothEnabled by remember {
        mutableStateOf(isBluetoothEnabled())
    }
    LaunchedEffect(Unit) {
        while (true) {

            bluetoothEnabled =
                isBluetoothEnabled()

            delay(5000)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {

            wifiConnected =
                isWifiConnected(context)

            delay(10000)
        }
    }

    var currentTime by remember {
        mutableStateOf(
            LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        )

    }
    var temperature by remember {
        mutableStateOf("--")
    }

    var weatherDescription by remember {
        mutableStateOf("Loading...")
    }
    var speed by remember {
        mutableStateOf("0")
    }

    var rpm by remember {
        mutableStateOf("0")
    }

    var fuel by remember {
        mutableStateOf("80")
    }

    LaunchedEffect(Unit) {
        while (true) {

            currentTime =
                LocalTime.now()
                    .format(
                        DateTimeFormatter.ofPattern("HH:mm:ss")
                    )

            delay(1000)
        }
    }

    LaunchedEffect(Unit) {

        try {

            weatherDescription = "Calling API..."

            val response =
                RetrofitClient.api.getWeather(
                    city = "Hyderabad",
                    apiKey = "a47a2a3b1eafa79797ad7391530b35f6"
                )

            temperature =
                response.main.temp.toInt().toString()

            weatherDescription =
                response.weather[0].description

        } catch (e: Exception) {

            weatherDescription =
                e.message ?: "Error"
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.honda_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TOP STATUS BAR

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "HONDA",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "Gooty, AP",
                        color = Color.LightGray
                    )
                }

                Text(
                    text = currentTime,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.weight(1f)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "🔋 $batteryPercent%",
                        color = Color.Green
                    )

                    Text(
                        text =
                            if (wifiConnected)
                                "📶 Connected"
                            else
                                "❌ Offline",
                        color = Color.White
                    )

                    Text(
                        text =
                            if (bluetoothEnabled)
                                "🔵 Bluetooth"
                            else
                                "⚫ Bluetooth Off",
                        color = Color.White
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // WEATHER

            Text(
                text = "Temp: $temperature°C",
                color = Color.Yellow,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = weatherDescription,
                color = Color.Cyan,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // VEHICLE STATUS CARD

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xCC202020),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "🚗 Speed : $speed km/h",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "⚙ RPM : $rpm",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "⛽ Fuel : $fuel %",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "✅ Engine OK",
                        color = Color.Green,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )


            // FIRST ROW

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    24.dp,
                    Alignment.CenterHorizontally
                )
            ) {

                DashboardTile(
                    title = "🗺 Navigation"
                ) {

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "google.navigation:q=Hyderabad".toUri()
                    )

                    intent.setPackage(
                        "com.google.android.apps.maps"
                    )

                    context.startActivity(intent)
                }

                DashboardTile(
                    title = "🎵 Music"
                ) {

                    val launchIntent =
                        context.packageManager
                            .getLaunchIntentForPackage(
                                "com.spotify.music"
                            )

                    if (launchIntent != null) {
                        context.startActivity(launchIntent)
                    }
                }
                DashboardTile(
                    title = "🚗 OBD"
                ) {

                    val intent =
                        Intent(
                            context,
                            ObdActivity::class.java
                        )

                    context.startActivity(intent)
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // SECOND ROW

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    24.dp,
                    Alignment.CenterHorizontally
                )
            ) {

                DashboardTile(
                    title = "📷 Camera"
                ) {

                    val intent =
                        Intent("android.media.action.IMAGE_CAPTURE")

                    context.startActivity(intent)
                }

                DashboardTile(
                    title = "⚙ Settings"
                ) {

                    val intent =
                        Intent(Settings.ACTION_SETTINGS)

                    context.startActivity(intent)
                }
            }   // End Row

        }       // End Column

    }       // End Box

}       // End HondaDashboard

@Composable
fun DashboardTile(
    title: String,
    onClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        modifier = Modifier
            .width(420.dp)
            .height(240.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB00020)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

fun getBatteryPercentage(context: android.content.Context): Int {

    val intent = context.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )

    val level = intent?.getIntExtra(
        BatteryManager.EXTRA_LEVEL,
        -1
    ) ?: -1

    val scale = intent?.getIntExtra(
        BatteryManager.EXTRA_SCALE,
        -1
    ) ?: -1

    return ((level * 100) / scale.toFloat()).toInt()
}

fun isWifiConnected(
    context: android.content.Context
): Boolean {

    val connectivityManager =
        context.getSystemService(
            android.content.Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

    val network =
        connectivityManager.activeNetwork ?: return false

    val capabilities =
        connectivityManager.getNetworkCapabilities(network)
            ?: return false

    return capabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_WIFI
    )
}

@Suppress("DEPRECATION")
fun isBluetoothEnabled(): Boolean {
    return BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false
}

