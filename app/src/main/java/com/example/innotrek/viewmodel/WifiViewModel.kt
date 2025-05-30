package com.example.innotrek.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innotrek.ui.screen.terminal.TcpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WifiViewModel : ViewModel() {
    private val _connectionStatus = MutableStateFlow(TcpClient.ConnectionStatus.DISCONNECTED)
    val connectionStatus: StateFlow<TcpClient.ConnectionStatus> = _connectionStatus

    private val _messages = MutableStateFlow(listOf("Esperando datos..."))
    val messages: StateFlow<List<String>> = _messages

    private lateinit var tcpClient: TcpClient

    fun initializeClient(ip: String, port: Int) {
        tcpClient = TcpClient(
            ip = ip,
            port = port,
            onMessageReceived = { message ->
                // Esta llamada ya se ejecuta en el hilo de procesamiento
                viewModelScope.launch {
                    _messages.value += message
                }
            },
            onConnectionStatusChanged = { status ->
                viewModelScope.launch {
                    _connectionStatus.value = status
                    when (status) {
                        TcpClient.ConnectionStatus.CONNECTED -> {
                            _messages.value += "Conexión establecida - ${getCurrentDateTime()}"
                        }
                        TcpClient.ConnectionStatus.DISCONNECTED -> {
                            _messages.value += "Se desconectó - ${getCurrentDateTime()}"
                        }
                        else -> {}
                    }
                }
            }
        )
    }

    fun toggleConnection() {
        when (connectionStatus.value) {
            TcpClient.ConnectionStatus.CONNECTED -> tcpClient.stopClient()
            else -> tcpClient.connect()
        }
    }

    fun clearTerminal() {
        _messages.value = listOf("Terminal limpiada - ${getCurrentDateTime()}")
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}