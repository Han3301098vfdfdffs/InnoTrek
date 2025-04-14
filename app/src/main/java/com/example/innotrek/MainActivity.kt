package com.example.innotrek.ui.theme

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.data.DeviceViewModel
import com.example.innotrek.ui.screens.DeviceConfigScreen
import com.example.innotrek.ui.screens.DeviceManagerScreen
import com.example.innotrek.ui.screens.MyMap


//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            InnoTrekTheme {
//                val navController = rememberNavController()
//                val deviceViewModel: DeviceViewModel = viewModel()
//
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    NavHost(navController = navController, startDestination = "device_manager") {
//                        composable("device_manager") {
//                            DeviceManagerScreen(
//                                navController = navController,
//                                viewModel = deviceViewModel
//                            )
//                        }
//                        composable("device_config") {
//                            DeviceConfigScreen(
//                                onSaveDevice = { newDevice ->
//                                    deviceViewModel.addDevice(newDevice)
//                                },
//                                onNavigateBack = {
//                                    navController.popBackStack()
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var hasLocationPermission by remember { mutableStateOf(false) }

            RequestLocationPermission { permissionGranted ->
                hasLocationPermission = permissionGranted
            }
            MyMap(hasLocationPermission = hasLocationPermission)
        }
    }
}

//Permisos de Ubicación
@Composable
fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        onResult(granted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}