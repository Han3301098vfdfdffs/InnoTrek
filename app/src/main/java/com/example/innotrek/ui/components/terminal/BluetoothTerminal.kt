package com.example.innotrek.ui.components.terminal

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.innotrek.R
import com.example.innotrek.data.BluetoothConfiguration
import com.example.innotrek.data.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BluetoothTerminal() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val bluetoothDevices = remember { mutableStateOf<List<BluetoothConfiguration>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            )
        ) {
            Text("Selecciona Dispositivo")
        }
    }

    if (showDialog) {
        BluetoothDevicesDialog(
            devices = bluetoothDevices.value,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun BluetoothDevicesDialog(
    devices: List<BluetoothConfiguration>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Dispositivos Bluetooth guardados",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (devices.isEmpty()) {
                    Text(
                        text = "No hay dispositivos guardados",
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn {
                        items(devices) { device ->
                            Text(
                                text = device.nombreDispositivo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                            Divider()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}