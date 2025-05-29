package com.example.innotrek.ui.screen.deviceconfig.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.data.DataDevices
import com.example.innotrek.ui.components.common.WarningMessage
import com.example.innotrek.ui.screen.deviceconfig.bluetooth.BluetoothConnectionContent
import com.example.innotrek.ui.screen.deviceconfig.ConnectionTypeSelector
import com.example.innotrek.viewmodel.DeviceConfigViewModel
import com.example.innotrek.ui.screen.deviceconfig.wifi.WifiConnectionContent
import com.example.innotrek.viewmodel.BluetoothViewModel

@Composable
fun DeviceConfigContent() {
    val devices = DataDevices().loadDevices() // Obtén la lista de dispositivos
    val deviceConfigViewModel: DeviceConfigViewModel = viewModel()
    val bluetoothViewModel: BluetoothViewModel = viewModel()

    // Estado para controlar si mostrar el mensaje de advertencia
    val showDeviceNotSelectedWarning = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Observar cambios cuando se guarda la configuración
        snapshotFlow { deviceConfigViewModel.connectionType }
            .collect { if (it.isEmpty()) bluetoothViewModel.resetSelection() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        DeviceConfigImageCarousel(
            devices = devices,
            selectedDeviceIndex = deviceConfigViewModel.selectedDeviceIndex,
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeviceConfigDropdown(
            devices = devices,
            selectedDeviceIndex = deviceConfigViewModel.selectedDeviceIndex,
            onDeviceSelected = {
                deviceConfigViewModel.selectDevice(it)
                showDeviceNotSelectedWarning.value = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConnectionTypeSelector(
            selectedType = deviceConfigViewModel.connectionType,
            onTypeSelected = {
                if (deviceConfigViewModel.selectedDeviceIndex == -1) {
                    showDeviceNotSelectedWarning.value = true
                } else {
                    deviceConfigViewModel.selectConnectionType(it)
                    showDeviceNotSelectedWarning.value = false
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (deviceConfigViewModel.connectionType) {
            "wifi" -> WifiConnectionContent(viewModel = deviceConfigViewModel)
            "bluetooth" -> BluetoothConnectionContent()
            else -> NoConnectionSelected()
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDeviceNotSelectedWarning.value) {
            WarningMessage("Por favor, selecciona un dispositivo primero")
        }
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