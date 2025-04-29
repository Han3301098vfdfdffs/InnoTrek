package com.example.innotrek.ui.components.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.data.DataDevices
import com.example.innotrek.ui.screens.devices.BluetoothConnectionContent
import com.example.innotrek.ui.screens.devices.ConnectionTypeSelector
import com.example.innotrek.ui.screens.devices.DeviceViewModel
import com.example.innotrek.ui.screens.devices.WifiConnectionFields


@Composable
fun DeviceContent() {
    val devices = DataDevices().loadDevices()
    val viewModel: DeviceViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DeviceImageCarousel(
            devices = devices,
            selectedDeviceIndex = viewModel.selectedDeviceIndex,
            onDeviceSelected = { viewModel.selectDevice(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeviceDropdown(
            devices = devices,
            selectedDeviceIndex = viewModel.selectedDeviceIndex,
            onDeviceSelected = { viewModel.selectDevice(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConnectionTypeSelector(
            selectedType = viewModel.connectionType,
            onTypeSelected = { viewModel.selectConnectionType(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (viewModel.connectionType) {
            "wifi" -> WifiConnectionFields(
                ipAddress = viewModel.ipAddress,
                port = viewModel.port,
                onIpChanged = { viewModel.updateIpAddress(it) },
                onPortChanged = { viewModel.updatePort(it) }
            )
            "bluetooth" -> BluetoothConnectionContent()
            else -> NoConnectionSelected()
        }

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.saveConfiguration() }
        )
    }
}

@Composable
fun NoConnectionSelected() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.LightGray.copy(alpha = 0.3f))
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CONEXIÓN NO SELECCIONADA",
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}