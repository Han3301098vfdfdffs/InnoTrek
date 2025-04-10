package com.example.innotrek.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.ui.draw.clip



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceConfigScreen() {
    val deviceOptions = listOf("ESP32", "Arduino", "Raspberry Pi", "STM32", "BeagleBone")
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(deviceOptions[0])

    var ipAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var connectionType by remember { mutableStateOf("") } // Wi-Fi or Bluetooth
    var deviceName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize() // Asegura que la columna ocupe toda la pantalla
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // Rellena todo el ancho disponible
                .height(70.dp) // Define una altura fija
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
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Configura tú dispositivo",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 25.sp,
                    color = Color.White
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            // Selector de tipo de dispositivo
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
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

            // Selector de tipo de conexión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .let {
                            if (connectionType == "Wi-Fi") it.background(Color(4, 139, 171))
                            else it.background(Color.LightGray)
                        }
                        .clickable { connectionType = "Wi-Fi" }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Wi-Fi", color = Color.Black)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .let {
                            if (connectionType == "Bluetooth") it.background(Color(4, 139, 171))
                            else it.background(Color.LightGray)
                        }
                        .clickable { connectionType = "Bluetooth" }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bluetooth", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para nombre de dispositivo Bluetooth
            if (connectionType == "Bluetooth") {
                OutlinedTextField(
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    label = { Text("Bluetooth Device Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Campo para dirección IP y puerto (solo para Wi-Fi)
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

                    println("Dispositivo: ${textFieldState.text}, Conexión: $connectionType, ${if (connectionType == "Wi-Fi") "IP: $ipAddress, Puerto: $port" else "Dispositivo Bluetooth: $deviceName"}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(6, 54, 97),
                    contentColor = Color(255, 255, 255)
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar configuración")
            }
        }
    }

}



@Preview(showBackground = true)
@Composable
fun DeviceRegistrationPreview() {
    DeviceConfigScreen()
}