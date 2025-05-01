package com.example.innotrek.ui.screens.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WifiConnectionContent(
    viewModel: DeviceViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Campo para la dirección IP
        TextField(
            value = viewModel.ipAddress,
            onValueChange = {
                viewModel.updateIpAddress(it)
                viewModel.validateIpAddress(it.text)
            },
            label = { Text("Dirección IP") },
            isError = viewModel.ipError.value != null,
            supportingText = { viewModel.ipError.value?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para el puerto
        TextField(
            value = viewModel.port,
            onValueChange = {
                viewModel.updatePort(it)
                viewModel.validatePort(it.text)
            },
            label = { Text("Puerto") },
            isError = viewModel.portError.value != null,
            supportingText = { viewModel.portError.value?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}