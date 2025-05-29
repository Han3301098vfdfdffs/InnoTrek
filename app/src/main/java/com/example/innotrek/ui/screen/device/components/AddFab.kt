package com.example.innotrek.ui.screen.device.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.navigation.AppScreens

@Composable
fun AddFab(navController: NavController) {
    FloatingActionButton(
        onClick = {
            navController.navigate(AppScreens.DeviceConfigScreen.route) {
                // No hacemos popUpTo para permitir volver al DeviceScreen
                launchSingleTop = true
            }
        },
        containerColor = colorResource(id = R.color.azul_fondo),
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Add, "Agregar Dispositivo")
    }
}