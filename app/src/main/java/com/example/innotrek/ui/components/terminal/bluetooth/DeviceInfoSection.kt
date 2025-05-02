package com.example.innotrek.ui.components.terminal.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.BluetoothConfiguration
import com.example.innotrek.viewmodel.BluetoothViewModel

@Composable
fun DeviceInfoSection(
    device: BluetoothConfiguration?,
    connectionState: BluetoothViewModel.ConnectionState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        device?.let { dev ->
            Text(
                text = "Dispositivo: ${dev.nombreDispositivo}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "MAC: ${dev.direccionMac}",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "tarjeta: ${dev.tarjeta}",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ConnectionStatusIndicator(connectionState)
        } ?: run {
            Text(
                text = "Ningún dispositivo seleccionado",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro)
            )
        }
    }
}
