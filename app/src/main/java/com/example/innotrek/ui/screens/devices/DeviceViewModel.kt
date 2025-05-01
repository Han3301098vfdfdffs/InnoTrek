package com.example.innotrek.ui.screens.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {

    val ipError = mutableStateOf<String?>(null)
    val portError = mutableStateOf<String?>(null)

    var selectedDeviceIndex by mutableIntStateOf(-1)
        private set

    var connectionType by mutableStateOf("")
        private set

    var ipAddress by mutableStateOf(TextFieldValue(""))
        private set

    var port by mutableStateOf(TextFieldValue(""))
        private set

    val isBluetoothDeviceSelected = mutableStateOf(false) // Nuevo estado
    val isFormValid = mutableStateOf(false)
    val isConfigurationSaved = mutableStateOf(false)


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

    fun validateIpAddress(ip: String): Boolean {
        val ipPattern = """^([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$""".toRegex()
        if (!ipPattern.matches(ip)) {
            ipError.value = "Formato de IP inválido"
            return false
        }

        val parts = ip.split(".").map { it.toInt() }
        if (parts.any { it > 255 }) {
            ipError.value = "Cada octeto debe ser ≤ 255"
            return false
        }

        ipError.value = null
        return true
    }

    fun validatePort(port: String): Boolean {
        if (port.isEmpty()) {
            portError.value = "El puerto no puede estar vacío"
            return false
        }

        val portNum = port.toIntOrNull()
        if (portNum == null) {
            portError.value = "El puerto debe ser un número"
            return false
        }

        if (portNum !in 1..65535) {
            portError.value = "El puerto debe estar entre 1 y 65535"
            return false
        }

        portError.value = null
        return true
    }

    fun updateBluetoothSelectionStatus(isSelected: Boolean) {
        isBluetoothDeviceSelected.value = isSelected
    }

    fun isFormValid(): Boolean {
        return when (connectionType) {
            "wifi" -> validateIpAddress(ipAddress.text) && validatePort(port.text)
            "bluetooth" -> isBluetoothDeviceSelected.value
            else -> false
        }
    }

    fun resetAllSelections() {
        // Resetear selección WiFi
        ipAddress = TextFieldValue("")
        port = TextFieldValue("")
        ipError.value = null
        portError.value = null

        // Resetear selección Bluetooth
        isBluetoothDeviceSelected.value = false

        // Resetear selección de dispositivo
        selectedDeviceIndex = -1
        connectionType = ""
    }

    fun updateValidation(
        connectionType: String,
        ipValid: Boolean,
        portValid: Boolean,
        bluetoothDeviceSelected: Boolean
    ) {
        isFormValid.value = !isConfigurationSaved.value && when (connectionType) {
            "wifi" -> ipValid && portValid
            "bluetooth" -> bluetoothDeviceSelected
            else -> false
        }
    }

    fun saveConfiguration() {
        if (!isFormValid.value) return
        // Lógica de guardado...
        isConfigurationSaved.value = true
        isFormValid.value = false
    }

}