package com.example.innotrek.data.model

data class Device(
    val type: String,
    val connectionType: String,
    val ipAddress: String? = null,
    val port: String? = null,
    val bluetoothName: String? = null

)