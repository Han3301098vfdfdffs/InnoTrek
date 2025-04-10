package com.example.innotrek.Model

import androidx.compose.ui.graphics.Color

data class Device(
    val type: String,
    val connectionType: String,
    val ipAddress: String? = null,
    val port: String? = null,
    val bluetoothName: String? = null

)