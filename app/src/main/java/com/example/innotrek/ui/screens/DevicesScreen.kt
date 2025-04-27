package com.example.innotrek.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.R
import com.example.innotrek.data.DataDevices
import com.example.innotrek.navigation.NavigationDrawerContent
import com.example.innotrek.responsiveTextSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(navController: NavController) {
    // Orientación
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Menu Lateral
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Fuentes
    val sizeVerticalFont = 20.sp
    val sizeHorizontalFont = 32.sp

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
                    title = {
                        Text(
                            text = "Dispositivos",
                            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                        )
                    },
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
            Box(
                modifier = Modifier
                    .padding(padding)
            ) {
                DeviceContent()
            }
        }
    }
}

@Composable
fun DeviceContent() {
    val devices = DataDevices().loadDevices()
    var selectedDeviceIndex by remember { mutableIntStateOf(-1) } // -1 means no device selected
    var isAutoScrolling by remember { mutableStateOf(selectedDeviceIndex == -1) }
    var connectionType by remember { mutableStateOf("") } // "wifi" or "bluetooth"
    var ipAddress by remember { mutableStateOf(TextFieldValue("")) }
    var port by remember { mutableStateOf(TextFieldValue("")) }
    var bluetoothDevices by remember { mutableStateOf(listOf("Dispositivo BT 1", "Dispositivo BT 2", "Dispositivo BT 3")) }
    var selectedBluetoothDevice by remember { mutableStateOf("") }
    var currentAutoScrollIndex by remember { mutableIntStateOf(0) }

    // Auto-scroll effect
    LaunchedEffect(isAutoScrolling, selectedDeviceIndex) {
        if (isAutoScrolling) {
            while (isAutoScrolling) {
                delay(3000) // 3 seconds delay between images
                currentAutoScrollIndex = (currentAutoScrollIndex + 1) % devices.size
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Device image display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            val displayIndex = if (selectedDeviceIndex != -1) selectedDeviceIndex else currentAutoScrollIndex

            if (devices.isNotEmpty()) {
                Image(
                    painter = painterResource(id = devices[displayIndex].drawableResourceId),
                    contentDescription = stringResource(id = devices[displayIndex].stringResourceId),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Device selection dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { isAutoScrolling = false }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember {
                mutableStateOf(
                    if (selectedDeviceIndex == -1) "Elegir dispositivo"
                    else "wtf"
                    //else stringResource(id = devices[selectedDeviceIndex].stringResourceId)
                )
            }

            Column {
                BasicTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                )

                if (expanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.White)
                            .border(1.dp, Color.Gray)
                    ) {
                        item {
                            Text(
                                text = "Elegir dispositivo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDeviceIndex = -1
                                        selectedText = "Elegir dispositivo"
                                        isAutoScrolling = true
                                        expanded = false
                                    }
                                    .padding(8.dp)
                            )
                        }
                        items(devices.size) { index ->
                            Text(
                                text = stringResource(id = devices[index].stringResourceId),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDeviceIndex = index
                                        selectedText = "wtf"
                                        //selectedText = stringResource(id = devices[index].stringResourceId)
                                        isAutoScrolling = false
                                        expanded = false
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Connection type selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { connectionType = "wifi" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (connectionType == "wifi") colorResource(id = R.color.azul_fondo) else Color.LightGray,
                    contentColor = if (connectionType == "wifi") Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("WiFi")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { connectionType = "bluetooth" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (connectionType == "bluetooth") colorResource(id = R.color.azul_fondo) else Color.LightGray,
                    contentColor = if (connectionType == "bluetooth") Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Bluetooth")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Connection details based on selection
        when (connectionType) {
            "wifi" -> {
                Column {
                    Text("Dirección IP:")
                    BasicTextField(
                        value = ipAddress,
                        onValueChange = { ipAddress = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Puerto:")
                    BasicTextField(
                        value = port,
                        onValueChange = { port = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                }
            }
            "bluetooth" -> {
                Column {
                    Text("Dispositivos Bluetooth disponibles:")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .border(1.dp, Color.Gray)
                    ) {
                        items(bluetoothDevices.size) { index ->
                            Text(
                                text = bluetoothDevices[index],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedBluetoothDevice = bluetoothDevices[index]
                                    }
                                    .padding(8.dp)
                                    .background(
                                        if (bluetoothDevices[index] == selectedBluetoothDevice)
                                            colorResource(id = R.color.azul_fondo).copy(alpha = 0.3f)
                                        else Color.Transparent
                                    )
                            )
                        }
                    }

                    if (selectedBluetoothDevice.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Conectado a: $selectedBluetoothDevice")
                    }
                }
            }
            else -> {
                // No connection type selected
                Text("Seleccione un tipo de conexión", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save button
        Button(
            onClick = { /* TODO: Save configuration */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = Color.White
            )
        ) {
            Text("Guardar configuración")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDevicesScreen() {
    DevicesScreen(navController = rememberNavController())
}