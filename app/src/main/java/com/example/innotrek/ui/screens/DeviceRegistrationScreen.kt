package com.example.innotrek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.model.Device
import com.example.innotrek.navigation.NavigationDrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceConfigScreen(
    navController: NavController,
    onSaveDevice: ((Device) -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    // --- Drawer state ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    title = { Text("Configura tu Dispositivo") },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            // Aquí aplicamos el padding del Scaffold
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // --- Tu formulario original ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val deviceOptions = listOf("ESP32", "Arduino", "Raspberry Pi", "STM32", "BeagleBone")
                        var expanded by remember { mutableStateOf(false) }
                        val textFieldState = rememberTextFieldState(deviceOptions[0])
                        var ipAddress by remember { mutableStateOf("") }
                        var port by remember { mutableStateOf("") }
                        var connectionType by remember { mutableStateOf("") }
                        var deviceName by remember { mutableStateOf("") }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = textFieldState,
                                readOnly = true,
                                label = { Text("Device Type") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                deviceOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            textFieldState.setTextAndPlaceCursorAtEnd(option)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Wi-Fi", "Bluetooth").forEach { type ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .background(
                                            if (connectionType == type)
                                                Color(4, 139, 171)
                                            else
                                                Color.LightGray
                                        )
                                        .clickable { connectionType = type }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(type, color = Color.Black)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (connectionType == "Bluetooth") {
                            OutlinedTextField(
                                value = deviceName,
                                onValueChange = { deviceName = it },
                                label = { Text("Bluetooth Device Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (connectionType == "Wi-Fi") {
                            OutlinedTextField(
                                value = ipAddress,
                                onValueChange = { ipAddress = it },
                                label = { Text("Device IP Address") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = port,
                                onValueChange = { port = it },
                                label = { Text("Port") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val device = Device(
                                    type = textFieldState.text.toString(),
                                    connectionType = connectionType,
                                    ipAddress = ipAddress.takeIf { connectionType == "Wi-Fi" },
                                    port = port.takeIf { connectionType == "Wi-Fi" },
                                    bluetoothName = deviceName.takeIf { connectionType == "Bluetooth" }
                                )
                                onSaveDevice?.invoke(device)
                                onNavigateBack?.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(6, 54, 97),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Guardar configuración")
                        }
                    }
                }
            }

        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewDeviceConfigScreen() {
//    // Para preview puedes pasar un NavController falso o null-check dentro
//    DeviceConfigScreen(
//        navController = rememberNavController(),
//        onSaveDevice = { /* ejemplo */ },
//        onNavigateBack = { /* ejemplo */ }
//    )
//}
