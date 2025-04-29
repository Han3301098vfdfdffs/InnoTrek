package com.example.innotrek.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.innotrek.data.DeviceViewModel
import com.example.innotrek.data.model.Device
import com.example.innotrek.ui.components.navigation.NavigationDrawerContent
import kotlinx.coroutines.launch


@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: DeviceViewModel
) {
    NavHost(navController = navController, startDestination = "device_manager") {
        composable("device_manager") {
            DeviceManagerScreen(navController = navController, viewModel = viewModel)
        }
        composable("device_config") {
            DeviceConfigScreen(
                navController = navController,      // asegúrate de pasarlo
                onSaveDevice = { device ->
                    viewModel.addDevice(device)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceManagerScreen(
    navController: NavHostController,
    viewModel: DeviceViewModel
) {
    // 1) Estado del drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val devices = viewModel.devices

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                onItemSelected = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dispositivos") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(4, 139, 171),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            // 2) Aquí va tu UI original, con padding del Scaffold
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
                    items(devices) { device ->
                        DeviceCard(device = device)
                    }
                }

                Button(
                    onClick = { navController.navigate("device_config") },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp)
                ) {
                    Text("Agregar dispositivo")
                }
            }
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