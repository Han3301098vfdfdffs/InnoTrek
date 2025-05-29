package com.example.innotrek.ui.screen.terminal.wifi

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.R
import com.example.innotrek.data.room.DatabaseProvider
import com.example.innotrek.data.room.WifiConfiguration
import com.example.innotrek.ui.screen.terminal.TcpClient
import com.example.innotrek.ui.utils.responsiveTextSize
import com.example.innotrek.viewmodel.WifiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun WifiTerminalScreen(viewModel: WifiViewModel) {
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val scrollState = rememberScrollState()
    val context =  LocalContext.current
    var showDeviceDialog by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<WifiConfiguration?>(null) }
    val wifiViewModel: WifiViewModel = viewModel()

    // Obtener dispositivos WiFi
    val wifiDevices by produceState(
        initialValue = emptyList(),
        key1 = showDeviceDialog
    ) {
        if (showDeviceDialog) {
            value = withContext(Dispatchers.IO) {
                try {
                    val db = DatabaseProvider.getDatabase(context)
                    db.wifiConfigurationDao().getAll()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }
    }

    LaunchedEffect(selectedDevice) {
        selectedDevice?.let { device ->
            wifiViewModel.initializeClient(ip = device.ip, port = device.puerto.toInt())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { showDeviceDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        text = "Seleccionar\nDispositivo",
                        fontSize = responsiveTextSize(14.sp, 22.sp)
                    )
                }
                selectedDevice?.let { device ->
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Dispositivo:\n${device.tarjeta}")
                        Text(
                            text = "Ip: ${device.ip}\nPort: ${device.puerto}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                } ?: Text(
                    text = "Dispositivo: Ninguno",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { viewModel.toggleConnection() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.width(150.dp),
                    enabled = selectedDevice != null
                ) {
                    Text(
                        text = when (connectionStatus) {
                            TcpClient.ConnectionStatus.CONNECTED -> "Desconectar"
                            else -> "Conectar"
                        },
                        fontSize = responsiveTextSize(14.sp, 22.sp)
                    )
                }
                ConnectionStatusIndicator(connectionStatus)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acciones (sin funcionalidad de guardado)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.clearTerminal() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.azul_fondo),
                    contentColor = colorResource(id = R.color.white)
                ),
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = "Limpiar\nTerminal",
                    fontSize = responsiveTextSize(14.sp, 22.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TerminalDisplay(
            messages = messages,
            scrollState = scrollState,
            modifier = Modifier.fillMaxWidth()
        )


    }


    // Auto-scroll
    LaunchedEffect(messages.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    // Diálogo para seleccionar dispositivo
    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Seleccionar Dispositivo WiFi") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (wifiDevices.isEmpty()) {
                        Text("No hay dispositivos guardados")
                    } else {
                        wifiDevices.forEach { device ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedDevice = device
                                        showDeviceDialog = false
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = device.tarjeta,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(text = "IP: ${device.ip}")
                                    Text(text = "Puerto: ${device.puerto}")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDeviceDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

// Los demás componentes (@Composable) permanecen iguales

@Composable
private fun ConnectionStatusIndicator(status: TcpClient.ConnectionStatus) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Estado:")
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when (status) {
                        TcpClient.ConnectionStatus.DISCONNECTED -> Color.Gray
                        TcpClient.ConnectionStatus.CONNECTING -> Color.Yellow
                        TcpClient.ConnectionStatus.CONNECTED -> Color.Green
                        TcpClient.ConnectionStatus.ERROR -> Color.Red
                    },
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun TerminalDisplay(
    messages: List<String>,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val fixedHeight = 350.dp  // Tamaño fijo que definas

    Text("Terminal:", style = MaterialTheme.typography.titleMedium)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(fixedHeight)  // Altura fija (no flexible)
            .border(1.dp, Color.Gray),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(8.dp)
        ) {
            messages.forEach { message ->
                Text(
                    message,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White
                )
            }
        }
    }
}