package com.example.innotrek.ui.screens.devices.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoomViewModel(application: Application): AndroidViewModel(application) {
    private val db = DatabaseSingleton.getDatabase(application)
    private val deviceDao = db.bluetoothDeviceDao()
    private val connectionDao = db.deviceConnectionDao()

    // Dispositivos Bluetooth
    val savedDevices: Flow<List<DeviceConfig>> = deviceDao.getAllDevices()

    // Conexiones guardadas
    val savedConnections: Flow<List<ConnectionConfig>> = connectionDao.getAllConnections()

    // Función para debuggear dispositivos
    fun debugDevices() {
        viewModelScope.launch {
            val devices = deviceDao.getAllDevices().first()
            Log.d("ROOM_DEBUG", "════════ Dispositivos (${devices.size}) ════════")
            devices.forEach { device ->
                Log.d("ROOM_DEBUG", """
                    MAC: ${device.macAddress}
                    Nombre: ${device.name} 
                    UUID: ${device.uuid}
                    Última conexión: ${device.lastConnected}
                    ──────────────────────────────────
                """.trimIndent())
            }
        }
    }

    // Función para debuggear conexiones
    fun debugConnections() {
        viewModelScope.launch {
            val connections = connectionDao.getAllConnections().first()
            Log.d("ROOM_DEBUG", "════════ Conexiones (${connections.size}) ════════")
            connections.forEach { conn ->
                Log.d("ROOM_DEBUG", """
                    ID: ${conn.id}
                    Dispositivo: ${conn.deviceName}
                    MAC: ${conn.macAddress}
                    IP: ${conn.ipAddress}:${conn.port}
                    Tipo: ${conn.connectionType}
                    Timestamp: ${conn.timestamp}
                    ──────────────────────────────────
                """.trimIndent())
            }
        }
    }

    fun saveDevice(deviceName: String, macAddress: String) {
        viewModelScope.launch {
            val device = DeviceConfig(
                macAddress = macAddress,
                name = deviceName,
                lastConnected = System.currentTimeMillis()
            )
            deviceDao.insertDevice(device)
            Log.d("DB_SAVE", "Dispositivo guardado: $deviceName - $macAddress")
            debugDevices() // Mostrar contenido actualizado
        }
    }

    fun saveConnection(
        deviceName: String,
        macAddress: String,
        ipAddress: String,
        port: String,
        connectionType: String
    ) {
        viewModelScope.launch {
            val connection = ConnectionConfig(
                deviceName = deviceName,
                macAddress = macAddress,
                ipAddress = ipAddress,
                port = port,
                connectionType = connectionType
            )
            connectionDao.insertConnection(connection)
            Log.d("DB_SAVE", "Conexión guardada: $deviceName - $ipAddress:$port ($connectionType)")
            debugConnections() // Mostrar contenido actualizado
        }
    }

    suspend fun deleteConnection(id: Int) {
        connectionDao.deleteConnection(id)
        Log.d("DB_DELETE", "Conexión eliminada ID: $id")
        debugConnections() // Mostrar contenido actualizado
    }
}