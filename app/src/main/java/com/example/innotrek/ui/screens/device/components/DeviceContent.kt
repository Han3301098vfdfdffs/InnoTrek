package com.example.innotrek.ui.screens.device.components

import androidx.compose.runtime.*
import androidx.navigation.NavController

// Enum para manejar el estado de la pantalla activa
enum class ConnectionScreen {
    WIFI, BLUETOOTH
}

@Composable
fun DeviceContent(navController: NavController) {
    BarWifiBlue(navController)
}





