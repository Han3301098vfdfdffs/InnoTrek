package com.example.innotrek.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wifi_configuration")
data class WifiConfiguration(
    @PrimaryKey(autoGenerate = true)
    val disp: Int = 0, // Identificador numérico único

    val tarjeta: String, // Nombre de la tarjeta
    val ip: String,      // Dirección IP
    val puerto: String   // Puerto
)

@Entity(tableName = "bluetooth_configuration")
data class BluetoothConfiguration(
    @PrimaryKey(autoGenerate = true)
    val disp: Int = 0, // Identificador numérico único

    val tarjeta: String,       // Nombre de la tarjeta
    val nombreDispositivo: String, // Nombre del dispositivo Bluetooth
    val direccionMac: String   // Dirección MAC
)