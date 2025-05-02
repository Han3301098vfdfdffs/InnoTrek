package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.DatabaseProvider
import com.example.innotrek.data.WifiConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun WifiTerminal(
    connectionState: WifiConnectionState,
    onDeviceSelected: (WifiConfiguration) -> Unit,
    onConnectClicked: () -> Unit
) {
    var state by remember { mutableStateOf(WifiTerminalState()) }
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()
    val indicatorColor = colorResource(id = R.color.gris_desactivado)

    // Actualizar terminal cuando cambia el estado
    LaunchedEffect(connectionState) {
        val newMessage = when (connectionState) {
            is WifiConnectionState.Connecting -> "Intentando conectar..."
            is WifiConnectionState.Connected -> "¡Conectado exitosamente!"
            is WifiConnectionState.Error -> "Error de conexión: ${connectionState.message}"
            else -> null
        }

        newMessage?.let {
            state = state.copy(
                terminalMessages = state.terminalMessages + it
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        // Primera fila: Botones y selección de dispositivo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButtons(
                onSelectDeviceClicked = {
                    coroutineScope.launch {
                        val devices = withContext(Dispatchers.IO) {
                            database.wifiConfigurationDao().getAll()
                        }
                        state = state.copy(wifiDevices = devices, showDialog = true)
                    }
                },
                onConnectClicked = {
                    state = state.copy(terminalMessages = emptyList())
                    onConnectClicked()
                },
                isConnectEnabled = false, // Forzar a deshabilitado temporalmente
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            DeviceInfo(
                device = state.selectedDevice,
                indicatorColor = indicatorColor,
                modifier = Modifier.weight(1f)
            )
        }

        // Terminal de mensajes
        TerminalDisplay(
            messages = state.terminalMessages,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )

        // Nueva sección para enviar texto
        MessageInput(
            isConnected = connectionState is WifiConnectionState.Connected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }

    // Diálogo para seleccionar dispositivos
    if (state.showDialog) {
        WifiDevicesDialog(
            devices = state.wifiDevices,
            onDismiss = { state = state.copy(showDialog = false) },
            onDeviceSelected = { device ->
                state = state.copy(
                    selectedDevice = device,
                    showDialog = false,
                    terminalMessages = state.terminalMessages + "Dispositivo seleccionado: ${device.tarjeta}"
                )
                onDeviceSelected(device)
            }
        )
    }
}

