package com.example.innotrek.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WifiConfiguration::class, BluetoothConfiguration::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wifiConfigurationDao(): WifiConfigurationDao
    abstract fun bluetoothConfigurationDao(): BluetoothConfigurationDao
}