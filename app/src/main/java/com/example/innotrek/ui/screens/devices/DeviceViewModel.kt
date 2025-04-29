package com.example.innotrek.ui.screens.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {
    var selectedDeviceIndex by mutableIntStateOf(-1)
        private set

    var connectionType by mutableStateOf("")
        private set

    var ipAddress by mutableStateOf(TextFieldValue(""))
        private set

    var port by mutableStateOf(TextFieldValue(""))
        private set

    fun selectDevice(index: Int) {
        selectedDeviceIndex = index
    }

    fun selectConnectionType(type: String) {
        connectionType = type
    }

    fun updateIpAddress(newIp: TextFieldValue) {
        ipAddress = newIp
    }

    fun updatePort(newPort: TextFieldValue) {
        port = newPort
    }

    fun saveConfiguration() {
        // Lógica para guardar la configuración
    }
}