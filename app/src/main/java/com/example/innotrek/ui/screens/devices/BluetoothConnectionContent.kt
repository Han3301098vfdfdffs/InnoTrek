package com.example.innotrek.ui.screens.devices

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
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
import com.example.innotrek.ui.screens.devices.bluetooth.BluetoothViewModel
import com.example.innotrek.ui.screens.devices.bluetooth.rememberBluetoothPermissionLauncher
import com.example.innotrek.ui.screens.devices.bluetooth.scanBluetoothPermissionLauncher

@SuppressLint("ContextCastToActivity")
@Composable
fun BluetoothConnectionContent() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val viewModel: BluetoothViewModel = viewModel()

    // Launcher para permisos BLUETOOTH_CONNECT
    val connectPermissionLauncher = rememberBluetoothPermissionLauncher(viewModel, context)

    // Launcher para permisos BLUETOOTH_SCAN
    val scanPermissionLauncher = scanBluetoothPermissionLauncher(viewModel, context, connectPermissionLauncher)

    // Launcher para activar Bluetooth
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.updateBluetoothState(result.resultCode == Activity.RESULT_OK)
    }
    // Verificar estado inicial
    LaunchedEffect(Unit) {
        viewModel.checkBluetoothState(context)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Botón para activar Bluetooth
        FilledTonalButton(
            onClick = {
                if (!viewModel.bluetoothEnabled.value) {
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
            Text(if (viewModel.bluetoothEnabled.value) "Abrir configuración Bluetooth" else "Activar Bluetooth")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.checkPermissionButton(context, activity, scanPermissionLauncher, connectPermissionLauncher)
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
        viewModel.selectedDevice.value?.let { device ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dispositivo seleccionado:", style = MaterialTheme.typography.titleMedium)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = device,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Mostrar dispositivos emparejados
        if (viewModel.pairedDevices.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dispositivos emparejados:", style = MaterialTheme.typography.titleMedium)
            viewModel.pairedDevices.forEach { device ->
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
                                viewModel.selectDevice(device)
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
        if (viewModel.devices.isNotEmpty()) {
            Text("Dispositivos encontrados:", style = MaterialTheme.typography.titleMedium)
            viewModel.devices.forEach { device ->
                Text(device, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
