package com.example.innotrek.ui.components.terminal.wifi

import com.example.innotrek.data.WifiConfiguration

sealed class WifiConnectionState {
    object Disconnected : WifiConnectionState()
    object Connecting : WifiConnectionState()
    data class Connected(val connectionInfo: String) : WifiConnectionState()
    data class Error(val message: String) : WifiConnectionState()
}

data class WifiTerminalState(
    val showDialog: Boolean = false,
    val wifiDevices: List<WifiConfiguration> = emptyList(),
    val selectedDevice: WifiConfiguration? = null,
    val terminalMessages: List<String> = emptyList()
)