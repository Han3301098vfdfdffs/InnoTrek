package com.example.innotrek.ui.screens.deviceconfig.bluetooth

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.example.innotrek.viewmodel.BluetoothViewModel

@Composable
fun rememberBluetoothPermissionLauncher(
    viewModel: BluetoothViewModel,
    context: Context
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadPairedDevices(context)
        }
    }

}

@Composable
fun scanBluetoothPermissionLauncher(
    viewModel: BluetoothViewModel,
    context: Context,
    connectPermissionLauncher: ActivityResultLauncher<String>
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startBluetoothScan(
                context = context,
                connectPermissionLauncher = connectPermissionLauncher)
        }
    }
}
