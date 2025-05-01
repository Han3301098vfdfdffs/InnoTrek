package com.example.innotrek.ui.screens.devices.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceConfig) // ← Solo se usa manualmente

    @Query("SELECT * FROM bluetooth_devices ORDER BY lastConnected DESC")
    fun getAllDevices(): Flow<List<DeviceConfig>>

    @Query("DELETE FROM bluetooth_devices WHERE macAddress = :macAddress")
    suspend fun deleteDevice(macAddress: String)
}


@Dao
interface DeviceConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(connection: ConnectionConfig)

    @Query("SELECT * FROM device_connections ORDER BY timestamp DESC")
    fun getAllConnections(): Flow<List<ConnectionConfig>>

    @Query("DELETE FROM device_connections WHERE id = :id")
    suspend fun deleteConnection(id: Int)
}