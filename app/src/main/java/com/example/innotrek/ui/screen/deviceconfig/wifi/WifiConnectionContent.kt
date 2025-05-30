package com.example.innotrek.ui.screen.deviceconfig.wifi

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.DataDevices
import com.example.innotrek.data.room.DatabaseProvider
import com.example.innotrek.data.room.WifiConfiguration
import com.example.innotrek.viewmodel.DeviceConfigViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WifiConnectionContent(
    viewModel: DeviceConfigViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val devices = DataDevices().loadDevices() // Obtén la lista de dispositivos


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


        Button(
            onClick = {
                // Validamos los campos antes de guardar
                val ipValid = viewModel.validateIpAddress(viewModel.ipAddress.text)
                val portValid = viewModel.validatePort(viewModel.port.text)

                if (ipValid && portValid) {
                    val wifiConfig = WifiConfiguration(
                        tarjeta = viewModel.getSelectedDeviceName(context, devices),
                        ip = viewModel.ipAddress.text,
                        puerto = viewModel.port.text
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val db = DatabaseProvider.getDatabase(context)
                            db.wifiConfigurationDao().insert(wifiConfig)

                            // Opcional: Mostrar mensaje de éxito en el hilo principal
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Configuración WiFi guardada", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            // Manejar errores
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text("Guardar Dispositivo")
        }
    }
}