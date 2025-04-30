package com.example.innotrek.ui.screens.devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothConnectionContent() {
    val context = LocalContext.current
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null) {
        Log.e("Bluetooth", "El dispositivo no soporta Bluetooth")
    } else if (!bluetoothAdapter.isEnabled) {
        Log.e("Bluetooth", "Bluetooth está desactivado")
    } else {
        Log.d("Bluetooth", "Bluetooth está activado y disponible")
    }
    val connectedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val bluetoothEnabled = remember { mutableStateOf(bluetoothAdapter?.isEnabled ?: false) }

    // Permisos (Android 12+)
    val bluetoothConnectPermission = rememberPermissionState(Manifest.permission.BLUETOOTH_CONNECT)
    val bluetoothScanPermission = rememberPermissionState(Manifest.permission.BLUETOOTH_SCAN)
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { bluetoothEnabled.value = bluetoothAdapter?.isEnabled ?: false }

    // BroadcastReceiver para detectar cambios en conexiones
    val bluetoothReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            if (!connectedDevices.any { d -> d.address == device.address }) {
                                connectedDevices.add(device)
                            }
                        }
                    }
                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            connectedDevices.removeAll { d -> d.address == device.address }
                        }
                    }
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        bluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
                        updateConnectedDevices(context, connectedDevices)
                    }
                }
            }
        }
    }



    // Registrar/desregistrar BroadcastReceiver
    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED) // Para cambios en el emparejamiento
        }
        context.registerReceiver(bluetoothReceiver, filter)

        onDispose {
            context.unregisterReceiver(bluetoothReceiver)
        }
    }

    // Manejo de permisos y actualización inicial
    LaunchedEffect(bluetoothConnectPermission.status, bluetoothScanPermission.status, locationPermission.status) {
        if (hasBluetoothPermissions(context)) {
            updateConnectedDevices(context, connectedDevices)
        }
    }

    // UI
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Sección de permisos
            PermissionSection(
                bluetoothConnectPermission = bluetoothConnectPermission,
                bluetoothScanPermission = bluetoothScanPermission,
                locationPermission = locationPermission
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Bluetooth
            Button(
                onClick = {
                    if (!bluetoothEnabled.value) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBluetoothLauncher.launch(enableBtIntent)
                    } else {
                        context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (bluetoothEnabled.value) "Abrir configuración Bluetooth" else "Activar Bluetooth")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de dispositivos conectados
            Text(
                text = "Dispositivos conectados (${connectedDevices.size}):",
                style = MaterialTheme.typography.titleMedium
            )

            if (connectedDevices.isEmpty()) {
                Text(
                    text = "No hay dispositivos Bluetooth conectados",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(connectedDevices) { device ->
                        BluetoothDeviceItem(device = device)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionSection(
    bluetoothConnectPermission: PermissionState,
    bluetoothScanPermission: PermissionState,
    locationPermission: PermissionState
) {
    val isAndroid12OrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    if (isAndroid12OrHigher) {
        if (!bluetoothConnectPermission.status.isGranted) {
            PermissionItem(
                permissionState = bluetoothConnectPermission,
                rationaleText = "Se necesita permiso para conectarse a dispositivos Bluetooth",
                deniedText = "Permiso de conexión Bluetooth denegado"
            )
        }

        if (!bluetoothScanPermission.status.isGranted) {
            PermissionItem(
                permissionState = bluetoothScanPermission,
                rationaleText = "Se necesita permiso para escanear dispositivos Bluetooth",
                deniedText = "Permiso de escaneo Bluetooth denegado"
            )
        }
    } else if (!locationPermission.status.isGranted) {
        PermissionItem(
            permissionState = locationPermission,
            rationaleText = "Se necesita permiso de ubicación para Bluetooth (Android 11 o inferior)",
            deniedText = "Permiso de ubicación denegado"
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionItem(
    permissionState: PermissionState,
    rationaleText: String,
    deniedText: String
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        if (permissionState.status.shouldShowRationale) {
            Text(text = rationaleText)
            Button(
                onClick = { permissionState.launchPermissionRequest() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Conceder permiso")
            }
        } else {
            Text(text = deniedText)
            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Abrir configuración")
            }
        }
    }
}

private fun updateConnectedDevices(context: Context, connectedDevices: MutableList<BluetoothDevice>) {
    connectedDevices.clear()
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter = bluetoothManager?.adapter

    if (bluetoothAdapter?.isEnabled == true) {
        try {
            // Lista de perfiles soportados y no obsoletos
            val supportedProfiles = mutableListOf<Int>().apply {
                add(BluetoothProfile.HEADSET)    // 1 - Perfil de auriculares/handsfree
                add(BluetoothProfile.A2DP)       // 2 - Perfil de audio avanzado
                add(BluetoothProfile.GATT)       // 7 - Dispositivos BLE
                add(BluetoothProfile.GATT_SERVER) // 8 - Servidor GATT

                // Agregar HID_HOST si está disponible (para teclados/ratones)
                runCatching {
                    BluetoothProfile::class.java.getField("HID_HOST").getInt(null)
                }.onSuccess { add(it) }

                // Agregar PAN si está disponible (redes personales)
                runCatching {
                    BluetoothProfile::class.java.getField("PAN").getInt(null)
                }.onSuccess { add(it) }
            }

            supportedProfiles.forEach { profile ->
                try {
                    bluetoothManager.getConnectedDevices(profile).forEach { device ->
                        if (!connectedDevices.any { it.address == device.address }) {
                            connectedDevices.add(device)
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    Log.d("Bluetooth", "Perfil $profile no soportado en este dispositivo")
                } catch (e: SecurityException) {
                    Log.e("Bluetooth", "Permiso denegado para perfil $profile", e)
                }
            }
        } catch (e: Exception) {
            Log.e("Bluetooth", "Error actualizando dispositivos conectados", e)
        }
    }
}

private fun hasBluetoothPermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BluetoothDeviceItem(device: BluetoothDevice) {
    val context = LocalContext.current
    val bluetoothConnectPermission = rememberPermissionState(Manifest.permission.BLUETOOTH_CONNECT)

    val deviceName = remember(device) {
        derivedStateOf {
            when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
                    device.name ?: "Dispositivo desconocido (${device.address})"
                }
                bluetoothConnectPermission.status.isGranted -> {
                    try {
                        device.name ?: "Dispositivo desconocido (${device.address})"
                    } catch (e: SecurityException) {
                        "Nombre oculto (permisos insuficientes)"
                    }
                }
                else -> {
                    "Nombre oculto (sin permisos)"
                }
            }
        }
    }

    val deviceType = remember(device) {
        when (device.bluetoothClass?.majorDeviceClass) {
            BluetoothClass.Device.Major.AUDIO_VIDEO -> "Auriculares/Altavoz"
            BluetoothClass.Device.Major.COMPUTER -> "Computadora"
            BluetoothClass.Device.Major.HEALTH -> "Dispositivo de salud"
            BluetoothClass.Device.Major.IMAGING -> "Impresora/Cámara"
            BluetoothClass.Device.Major.PERIPHERAL -> "Teclado/Ratón"
            BluetoothClass.Device.Major.PHONE -> "Teléfono"
            BluetoothClass.Device.Major.TOY -> "Juguete"
            BluetoothClass.Device.Major.WEARABLE -> "Wearable"
            BluetoothClass.Device.Major.UNCATEGORIZED -> "Dispositivo genérico"
            else -> "Tipo desconocido"
        }
    }

    // Verificar conexión de forma compatible
    val isConnected = remember(device) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothManager?.getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = deviceName.value,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                val icon = when (device.bluetoothClass?.majorDeviceClass) {
                BluetoothClass.Device.Major.AUDIO_VIDEO -> Icons.Default.Headphones
                BluetoothClass.Device.Major.COMPUTER -> Icons.Default.Computer
                BluetoothClass.Device.Major.PHONE -> Icons.Default.Phone
                BluetoothClass.Device.Major.WEARABLE -> Icons.Default.Watch
                BluetoothClass.Device.Major.PERIPHERAL -> Icons.Default.Keyboard
                else -> Icons.Default.Devices
            }

                Icon(
                    imageVector = icon,
                    contentDescription = deviceType,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Tipo: $deviceType",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Dirección: ${device.address}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Estado: ${if (isConnected) "Conectado" else "No conectado"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}