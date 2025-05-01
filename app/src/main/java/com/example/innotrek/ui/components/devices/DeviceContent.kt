package com.example.innotrek.ui.components.devices

import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.TextField
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
import com.example.innotrek.data.model.Device
import com.example.innotrek.ui.components.common.WarningMessage
import com.example.innotrek.ui.screens.devices.BluetoothConnectionContent
import com.example.innotrek.ui.screens.devices.ConnectionTypeSelector
import com.example.innotrek.ui.screens.devices.DeviceViewModel
import com.example.innotrek.ui.screens.devices.WifiConnectionContent
import com.example.innotrek.ui.screens.devices.bluetooth.BluetoothViewModel
import com.example.innotrek.ui.screens.devices.room.RoomViewModel


@Composable
fun DeviceContent() {
    val devices = DataDevices().loadDevices()
    val deviceViewModel: DeviceViewModel = viewModel()
    val roomViewModel: RoomViewModel = viewModel()
    val bluetoothViewModel: BluetoothViewModel = viewModel()

    // Estado para controlar si mostrar el mensaje de advertencia
    val showDeviceNotSelectedWarning = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Observar cambios cuando se guarda la configuración
        snapshotFlow { deviceViewModel.connectionType }
            .collect { if (it.isEmpty()) bluetoothViewModel.resetSelection() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        DeviceImageCarousel(
            devices = devices,
            selectedDeviceIndex = deviceViewModel.selectedDeviceIndex,
            onDeviceSelected = {
                deviceViewModel.selectDevice(it)
                showDeviceNotSelectedWarning.value = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeviceDropdown(
            devices = devices,
            selectedDeviceIndex = deviceViewModel.selectedDeviceIndex,
            onDeviceSelected = {
                deviceViewModel.selectDevice(it)
                showDeviceNotSelectedWarning.value = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConnectionTypeSelector(
            selectedType = deviceViewModel.connectionType,
            onTypeSelected = {
                if (deviceViewModel.selectedDeviceIndex == -1) {
                    showDeviceNotSelectedWarning.value = true
                } else {
                    deviceViewModel.selectConnectionType(it)
                    showDeviceNotSelectedWarning.value = false
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (deviceViewModel.connectionType) {
            "wifi" -> WifiConnectionContent(viewModel = deviceViewModel)
            "bluetooth" -> BluetoothConnectionContent()
            else -> NoConnectionSelected()
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDeviceNotSelectedWarning.value) {
            WarningMessage("Por favor, selecciona un dispositivo primero")
        }
        SaveButton(
            enabled = deviceViewModel.isFormValid(),
            onClick = {
                roomViewModel.debugDevices()
                roomViewModel.debugConnections()
                deviceViewModel.saveConfiguration()
                bluetoothViewModel.resetSelection()
                deviceViewModel.resetAllSelections()
            }
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