package com.example.innotrek.ui.screens.terminal

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.ui.components.device.ConnectionScreen
import com.example.innotrek.ui.screens.terminal.bluetooth.BluetoothTerminalScreen
import com.example.innotrek.ui.screens.terminal.wifi.WifiTerminalScreen
import com.example.innotrek.viewmodel.BluetoothViewModel
import com.example.innotrek.viewmodel.TerminalViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BarTerminal() {
    var activeScreen by remember { mutableStateOf(ConnectionScreen.WIFI) }
    val terminalViewModel: TerminalViewModel = viewModel()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
        } else {
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (activeScreen) {
            ConnectionScreen.WIFI -> {
                WifiTerminalScreen(viewModel = terminalViewModel)
            }
            ConnectionScreen.BLUETOOTH -> {
                BluetoothTerminalScreen(
                    requestPermission = { permission ->
                        requestPermissionLauncher.launch(permission)
                    }
                    )
            }
        }

        BottomConnectionBar(
            activeScreen = activeScreen,
            onWifiClick = { activeScreen = ConnectionScreen.WIFI },
            onBluetoothClick = { activeScreen = ConnectionScreen.BLUETOOTH },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BottomConnectionBar(
    activeScreen: ConnectionScreen,
    onWifiClick: () -> Unit,
    onBluetoothClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConnectionButton(
                iconRes = R.drawable.ic_wifi,
                contentDescription = "WiFi",
                isActive = activeScreen == ConnectionScreen.WIFI,
                onClick = onWifiClick,
                activeColor = Color.Green
            )

            ConnectionButton(
                iconRes = R.drawable.ic_bluetooth,
                contentDescription = "Bluetooth",
                isActive = activeScreen == ConnectionScreen.BLUETOOTH,
                onClick = onBluetoothClick,
                activeColor = Color.Blue
            )
        }
    }
}

@Composable
private fun ConnectionButton(
    iconRes: Int,
    contentDescription: String,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = if (isActive) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )
    }
}