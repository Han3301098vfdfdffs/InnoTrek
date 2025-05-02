package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.innotrek.R
import com.example.innotrek.data.WifiConfiguration

@Composable
fun WifiDevicesDialog(
    devices: List<WifiConfiguration>,
    onDismiss: () -> Unit,
    onDeviceSelected: (WifiConfiguration) -> Unit
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
                    text = "Dispositivos WiFi guardados",
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
                            Card(
                                onClick = { onDeviceSelected(device) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(id = R.color.azul_claro)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = device.tarjeta,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "IP: ${device.ip}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorResource(id = R.color.black)
                                    )
                                    Text(
                                        text = "Puerto: ${device.puerto}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorResource(id = R.color.black)
                                    )
                                }
                            }
                            HorizontalDivider()
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