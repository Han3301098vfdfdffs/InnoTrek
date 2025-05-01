package com.example.innotrek.ui.screens.devices.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bluetooth_devices")
data class DeviceConfig(
    @PrimaryKey val macAddress: String,  // Clave primaria (única por dispositivo)
    val name: String,                    // Nombre del dispositivo (ej. "HC-05")
    val uuid: String = "00001101-0000-1000-8000-00805F9B34FB", // UUID por defecto para SPP
    val lastConnected: Long = System.currentTimeMillis() // Fecha de última conexión
)

@Entity(tableName = "device_connections")
data class ConnectionConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceName: String,
    val macAddress: String,
    val ipAddress: String,
    val port: String,
    val connectionType: String, // "wifi" o "bluetooth"
    val timestamp: Long = System.currentTimeMillis()
)