package com.example.innotrek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.innotrek.data.DeviceViewModel
import com.example.innotrek.model.Device


@Composable
fun NavigationGraph(navController: NavHostController, viewModel: DeviceViewModel) {
    NavHost(navController = navController, startDestination = "device_manager") {
        composable("device_manager") {
            DeviceManagerScreen(navController = navController, viewModel = viewModel)
        }
        composable("device_config") {
            DeviceConfigScreen(
                onSaveDevice = { device ->

                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
@Composable
fun DeviceManagerScreen(navController: NavHostController,     viewModel: DeviceViewModel) {
    val devices = viewModel.devices

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(Color(4, 139, 171))
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                IconButton(
                    onClick = { /* Acción del ícono de menú */ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Dispositivos",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(devices) { device ->
                DeviceCard(device = device)
            }
        }

        Button(
            onClick = {
                navController.navigate("device_config")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar dispositivo")
        }

    }
}





@Composable
fun DeviceCard(device: Device) {
    Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tipo: ${device.type}", style = MaterialTheme.typography.titleMedium)
                    Text("Conexión: ${device.connectionType}")

                    if (device.connectionType == "Wi-Fi") {
                        Text("IP: ${device.ipAddress}")
                        Text("Puerto: ${device.port}")
                    } else {
                        Text("Bluetooth: ${device.bluetoothName}")
                    }
                }
    }
}


/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDeviceManagerScreen() {
    DeviceManagerScreen(
        NavController()
    )
}
*/