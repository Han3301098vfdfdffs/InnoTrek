package com.example.innotrek.ui.components.terminal.bluetooth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.BluetoothConfiguration
import com.example.innotrek.data.DatabaseProvider
import com.example.innotrek.viewmodel.BluetoothViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BluetoothTerminal(
    connectionState: BluetoothViewModel.ConnectionState,
    onDeviceSelected: (BluetoothConfiguration) -> Unit,
    onConnectClicked: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val bluetoothDevices = remember { mutableStateOf<List<BluetoothConfiguration>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var selectedDevice by remember { mutableStateOf<BluetoothConfiguration?>(null) }
    val terminalMessages = remember { mutableStateListOf<String>() }

    // Actualizar terminal cuando cambia el estado
    LaunchedEffect(connectionState) {
        when (connectionState) {
            is BluetoothViewModel.ConnectionState.Connecting -> {
                terminalMessages.add("Intentando conectar...")
            }
            is BluetoothViewModel.ConnectionState.Connected -> {
                terminalMessages.add("¡Conectado exitosamente!")
            }
            is BluetoothViewModel.ConnectionState.Error -> {
                terminalMessages.add("Error de conexión: ${connectionState.message}")
            }
            else -> {}
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
            // Columna para los botones
            Column(modifier = Modifier.weight(1f)) {
                // Botón para seleccionar dispositivo
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val devices = withContext(Dispatchers.IO) {
                                database.bluetoothConfigurationDao().getAll()
                            }
                            bluetoothDevices.value = devices
                            showDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar\nDispositivo")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para conectar
                Button(
                    onClick = {
                        terminalMessages.clear()
                        onConnectClicked()
                    },
                    enabled = selectedDevice != null &&
                            connectionState !is BluetoothViewModel.ConnectionState.Connected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.verde_exito),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conectar")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Columna para la información del dispositivo
            DeviceInfoSection(
                device = selectedDevice,
                connectionState = connectionState,
                modifier = Modifier.weight(1f)
            )
        }

        // Terminal de mensajes
        TerminalMessagesDisplay(
            messages = terminalMessages,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )

        // Sección para enviar mensajes
        MessageInputSection(
            isConnected = connectionState is BluetoothViewModel.ConnectionState.Connected,
            onSendClicked = { /* Implementar lógica de envío */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }

    // Diálogo para seleccionar dispositivos
    if (showDialog) {
        BluetoothDevicesDialog(
            devices = bluetoothDevices.value,
            onDismiss = { showDialog = false },
            onDeviceSelected = { device ->
                selectedDevice = device
                onDeviceSelected(device)
                showDialog = false
                terminalMessages.add("Dispositivo seleccionado: ${device.nombreDispositivo}")
            }
        )
    }
}