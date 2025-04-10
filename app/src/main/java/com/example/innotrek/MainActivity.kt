package com.example.innotrek.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.Data.DeviceViewModel
import com.example.innotrek.ui.theme.Screens.DeviceConfigScreen
import com.example.innotrek.ui.theme.Screens.DeviceManagerScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InnoTrekTheme {
                val navController = rememberNavController()
                val deviceViewModel: DeviceViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "device_manager") {
                        composable("device_manager") {
                            DeviceManagerScreen(
                                navController = navController,
                                viewModel = deviceViewModel
                            )
                        }
                        composable("device_config") {
                            DeviceConfigScreen(
                                onSaveDevice = { newDevice ->
                                    deviceViewModel.addDevice(newDevice)
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}