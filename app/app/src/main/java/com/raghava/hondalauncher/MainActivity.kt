package com.raghava.hondalauncher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.sp

import java.time.LocalTime
import java.time.format.DateTimeFormatter

import kotlinx.coroutines.delay
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

    val currentTime = LocalTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HONDA",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hyderabad • 32°C",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentTime,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=Hyderabad")
                )

                intent.setPackage(
                    "com.google.android.apps.maps"
                )

                context.startActivity(intent)
            }
        ) {
            Text("Navigation")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                val launchIntent =
                    context.packageManager
                        .getLaunchIntentForPackage(
                            "com.spotify.music"
                        )

                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            }
        ) {
            Text("Music")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                val intent =
                    Intent("android.media.action.IMAGE_CAPTURE")

                context.startActivity(intent)
            }
        ) {
            Text("Camera")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                val intent =
                    Intent(Settings.ACTION_SETTINGS)

                context.startActivity(intent)
            }
        ) {
            Text("Settings")
        }
    }
}