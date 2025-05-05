package com.example.innotrek.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WifiConfigurationDao {
    @Insert
    suspend fun insert(config: WifiConfiguration)

    @Query("SELECT * FROM wifi_configuration")
    suspend fun getAll(): List<WifiConfiguration>

    @Query("SELECT * FROM wifi_configuration WHERE tarjeta = :deviceName")
    suspend fun getByDeviceName(deviceName: String): List<WifiConfiguration>

    @Query("SELECT * FROM wifi_configuration WHERE ip = :ipAddress")
    suspend fun getByIpAddress(ipAddress: String): List<WifiConfiguration>

    // Eliminar una configuración por ID
    @Query("DELETE FROM wifi_configuration WHERE disp = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface BluetoothConfigurationDao {
    @Insert
    suspend fun insert(config: BluetoothConfiguration)

    @Query("SELECT * FROM bluetooth_configuration")
    suspend fun getAll(): List<BluetoothConfiguration>

    // Eliminar una configuración por ID
    @Query("DELETE FROM bluetooth_configuration WHERE disp = :id")
    suspend fun deleteById(id: Int)
}