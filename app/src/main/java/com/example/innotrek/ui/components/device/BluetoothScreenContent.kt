package com.example.innotrek.ui.components.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.AppDatabase
import com.example.innotrek.data.BluetoothConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BluetoothScreenContent(
    modifier: Modifier = Modifier,
    database: AppDatabase,
    onDelete: () -> Unit = {},
    onAddDevice: () -> Unit
) {
    var bluetoothConfiguration by remember { mutableStateOf(emptyList<BluetoothConfiguration>()) }
    val coroutineScope = rememberCoroutineScope()
    var configToDelete by remember { mutableStateOf<BluetoothConfiguration?>(null) }

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val configs = withContext(Dispatchers.IO) {
                database.bluetoothConfigurationDao().getAll()
            }
            bluetoothConfiguration = configs
        }
    }

    // Diálogo de confirmación
    if (configToDelete != null) {
        AlertDialog(
            onDismissRequest = { configToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar esta configuración?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            configToDelete?.let { config ->
                                withContext(Dispatchers.IO) {
                                    database.bluetoothConfigurationDao().deleteById(config.disp)
                                }
                                bluetoothConfiguration = bluetoothConfiguration.filter { it.disp != config.disp }
                                onDelete()
                            }
                            configToDelete = null
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { configToDelete = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bluetoothConfiguration.isEmpty()) {
            // Mostrar mensaje y botón cuando no hay dispositivos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No hay dispositivos Bluetooth configurados",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onAddDevice,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text("Agregar dispositivo")
                }
            }
        } else {
            // Mostrar la lista si hay dispositivos
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(bluetoothConfiguration) { config ->
                    ConfigurationCard(
                        config = config,
                        onDelete = { configToDelete = config }
                    )
                }
            }
        }
    }
}