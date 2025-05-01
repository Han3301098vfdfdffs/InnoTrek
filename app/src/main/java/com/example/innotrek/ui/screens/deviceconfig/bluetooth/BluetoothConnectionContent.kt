package com.example.innotrek.ui.screens.deviceconfig.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.R
import com.example.innotrek.data.BluetoothConfiguration
import com.example.innotrek.data.DataDevices
import com.example.innotrek.data.DatabaseProvider
import com.example.innotrek.ui.screens.deviceconfig.DeviceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ContextCastToActivity")
@Composable
fun BluetoothConnectionContent() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val bluetoothViewModel: BluetoothViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val devices = DataDevices().loadDevices() // Obtén la lista de dispositivos

    // Launcher para permisos BLUETOOTH_CONNECT
    val connectPermissionLauncher = rememberBluetoothPermissionLauncher(bluetoothViewModel, context)

    // Launcher para permisos BLUETOOTH_SCAN
    val scanPermissionLauncher = scanBluetoothPermissionLauncher(bluetoothViewModel, context, connectPermissionLauncher)

    // Launcher para activar Bluetooth
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        bluetoothViewModel.updateBluetoothState(result.resultCode == Activity.RESULT_OK)
    }
    // Verificar estado inicial
    LaunchedEffect(Unit) {
        bluetoothViewModel.checkBluetoothState(context)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Botón para activar Bluetooth
        FilledTonalButton(
            onClick = {
                if (!bluetoothViewModel.bluetoothEnabled.value) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    enableBluetoothLauncher.launch(enableBtIntent)
                } else {
                    context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = Color.White
            )
        ) {
            Text(if (bluetoothViewModel.bluetoothEnabled.value) "Abrir configuración Bluetooth" else "Activar Bluetooth")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                bluetoothViewModel.checkPermissionButton(context, activity, scanPermissionLauncher, connectPermissionLauncher)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text("Seleccciona tu dispositivo")
        }

        // Mostrar dispositivo seleccionado (si hay uno)
        bluetoothViewModel.selectedDevice.value?.let { device ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dispositivo seleccionado:", style = MaterialTheme.typography.titleMedium)
            Card(
                modifier = Modifier
                    .clickable {
                        bluetoothViewModel.selectDevice(device)
                    }
            ) {
                Text(
                    text = device,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }?: run {
            if (bluetoothViewModel.bluetoothEnabled.value) {
                Text(
                    text = "No hay dispositivo seleccionado",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        // Mostrar dispositivos emparejados
        if (bluetoothViewModel.pairedDevices.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dispositivos emparejados:", style = MaterialTheme.typography.titleMedium)
            bluetoothViewModel.pairedDevices.forEach { device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                bluetoothViewModel.selectDevice(device)
                            }
                    ) {
                        Text(
                            text = device,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        // Mostrar nuevos dispositivos encontrados
        if (bluetoothViewModel.devices.isNotEmpty()) {
            Text("Dispositivos encontrados:", style = MaterialTheme.typography.titleMedium)
            bluetoothViewModel.devices.forEach { device ->
                Text(device, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        Button(
            onClick = {
                // Validamos los campos antes de guardar
                bluetoothViewModel.selectedDeviceName.value?.let { deviceName ->
                    bluetoothViewModel.selectedDeviceAddress.value?.let { macAddress ->
                        val bluetoothConfig = BluetoothConfiguration(
                            tarjeta = deviceViewModel.getSelectedDeviceName(context, devices),
                            nombreDispositivo = deviceName,
                            direccionMac = macAddress
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val db = DatabaseProvider.getDatabase(context)
                                db.bluetoothConfigurationDao().insert(bluetoothConfig) // Asegúrate de que estás usando el DAO correcto

                                // Opcional: Mostrar mensaje de éxito en el hilo principal
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Configuración Bluetooth guardada", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                // Manejar errores
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } ?: run {
                    Toast.makeText(context, "Selecciona un dispositivo primero", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Dispositivo")
        }
    }
}
