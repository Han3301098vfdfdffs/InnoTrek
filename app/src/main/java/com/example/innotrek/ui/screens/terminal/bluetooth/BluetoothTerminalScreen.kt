package com.example.innotrek.ui.screens.terminal.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.widget.Toast
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
import com.example.innotrek.data.room.BluetoothConfiguration
import com.example.innotrek.data.room.DatabaseProvider
import com.example.innotrek.ui.screens.deviceconfig.bluetooth.rememberBluetoothPermissionLauncher
import com.example.innotrek.ui.screens.deviceconfig.bluetooth.scanBluetoothPermissionLauncher
import com.example.innotrek.ui.utils.responsiveTextSize
import com.example.innotrek.viewmodel.BluetoothViewModel
import com.example.innotrek.viewmodel.TerminalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@SuppressLint("ContextCastToActivity")
@Composable
fun BluetoothTerminalScreen(
    requestPermission: (String) -> Unit
) {

    val scrollState = rememberScrollState()
    var showDeviceDialog by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<BluetoothConfiguration?>(null) }
    val bluetoothViewModel: BluetoothViewModel = viewModel()
    val terminalViewModel: TerminalViewModel = viewModel()
    val receivedMessages by bluetoothViewModel.receivedMessages.collectAsState()
    val connectionState by bluetoothViewModel.connectionState.collectAsState()
    val activity = LocalContext.current as Activity
    val context = LocalContext.current


    // Obtener dispositivos Bluetooth guardados
    val bluetoothDevices by produceState(
        initialValue = emptyList(),
        key1 = showDeviceDialog
    ) {
        if (showDeviceDialog) {
            value = withContext(Dispatchers.IO) {
                try {
                    val db = DatabaseProvider.getDatabase(context)
                    db.bluetoothConfigurationDao().getAll()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }
    }

    val connectPermissionLauncher = rememberBluetoothPermissionLauncher(bluetoothViewModel, context)
    val scanPermissionLauncher = scanBluetoothPermissionLauncher(bluetoothViewModel, context, connectPermissionLauncher)

    // Verificar estado inicial
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermission(Manifest.permission.BLUETOOTH_CONNECT)
            requestPermission(Manifest.permission.BLUETOOTH_SCAN)
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        bluetoothViewModel.checkBluetoothState(context)
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
                    onClick = {
                        bluetoothViewModel.checkPermissionButton(
                            context,
                            activity,
                            scanPermissionLauncher,
                            connectPermissionLauncher
                        )
                        showDeviceDialog = true
                    },
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
                            text = "Nombre: ${device.nombreDispositivo}\nMAC: ${device.direccionMac}",
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
                    onClick = {
                        selectedDevice?.let { device ->
                            bluetoothViewModel.selectedDeviceAddress.value = device.direccionMac
                            when (connectionState) {
                                is BluetoothViewModel.ConnectionState.Disconnected,
                                is BluetoothViewModel.ConnectionState.Error -> {
                                    bluetoothViewModel.connectToDeviceByMac(context, device.direccionMac)
                                }
                                is BluetoothViewModel.ConnectionState.Connecting -> {
                                    // Opcional: puedes agregar un mensaje de "Ya conectando..."
                                }
                                is BluetoothViewModel.ConnectionState.Connected -> {
                                    bluetoothViewModel.disconnect()
                                }
                            }
                        } ?: run {
                            Toast.makeText(context, "Selecciona un dispositivo primero", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.width(150.dp),
                    enabled = selectedDevice != null
                ) {
                    Text(
                        text = when (connectionState) {
                            is BluetoothViewModel.ConnectionState.Connected -> "Desconectar"
                            else -> "Conectar"
                        },
                        fontSize = responsiveTextSize(14.sp, 22.sp)
                    )
                }
                BluetoothConnectionStatusIndicator(connectionState)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { terminalViewModel.clearTerminal() },
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
            messages = receivedMessages,
            scrollState = scrollState,
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Diálogo para seleccionar dispositivo Bluetooth
    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Seleccionar Dispositivo Bluetooth") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (bluetoothDevices.isEmpty()) {
                        Text("No hay dispositivos guardados")
                    } else {
                        bluetoothDevices.forEach { device ->
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
                                    Text(text = "Nombre: ${device.nombreDispositivo}")
                                    Text(text = "MAC: ${device.direccionMac}")
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

    // Auto-scroll
    LaunchedEffect(receivedMessages.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
}

// Los demás componentes (@Composable) permanecen iguales

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
            .border(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(8.dp)
        ) {
            messages.forEach { message ->
                Text(
                    text = message,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun BluetoothConnectionStatusIndicator(connectionState: BluetoothViewModel.ConnectionState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Estado:")
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when (connectionState) {
                        is BluetoothViewModel.ConnectionState.Disconnected -> Color.Gray
                        is BluetoothViewModel.ConnectionState.Connecting -> Color.Yellow
                        is BluetoothViewModel.ConnectionState.Connected -> Color.Green
                        is BluetoothViewModel.ConnectionState.Error -> Color.Red
                    },
                    shape = CircleShape
                )
        )
    }
}