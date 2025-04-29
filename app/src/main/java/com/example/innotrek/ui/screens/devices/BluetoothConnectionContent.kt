package com.example.innotrek.ui.screens.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@Composable
fun BluetoothConnectionContent(modifier: Modifier = Modifier) {
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    Column(modifier = modifier) {
        Text(
            text = "Dispositivos Bluetooth disponibles:",
            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
        )
        // Bluetooth Pendiente
    }
}