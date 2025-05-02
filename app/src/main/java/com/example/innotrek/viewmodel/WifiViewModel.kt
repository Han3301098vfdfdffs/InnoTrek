package com.example.innotrek.viewmodel

import com.example.innotrek.data.WifiConfiguration
import androidx.lifecycle.ViewModel
import com.example.innotrek.ui.components.terminal.wifi.WifiConnectionState

class WifiViewModel : ViewModel() {
    // Estado inicial desconectado
    var connectionState: WifiConnectionState = WifiConnectionState.Disconnected
        private set

    // Función para conectar (simulada)
    fun connectToDevice(device: WifiConfiguration) {
        connectionState = WifiConnectionState.Connecting
        // Aquí iría la lógica real de conexión WiFi
        // Por ahora simulamos una conexión exitosa después de 2 segundos
        // En una aplicación real, esto sería una llamada a tu librería WiFi
        /*CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            connectionState = WifiConnectionState.Connected("Conexión WiFi establecida")
        }*/
    }
}