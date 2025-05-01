package com.example.innotrek.ui.screens.devices.room

import androidx.room.Database
import androidx.room.RoomDatabase

// AppDatabase.kt
@Database(
    entities = [DeviceConfig::class, ConnectionConfig::class], // Añade la nueva entidad
    version = 2 // Incrementa la versión
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bluetoothDeviceDao(): BluetoothDeviceDao
    abstract fun deviceConnectionDao(): DeviceConnectionDao // Añade el nuevo DAO
}

