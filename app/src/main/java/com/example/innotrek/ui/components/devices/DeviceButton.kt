package com.example.innotrek.ui.components.devices

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.R
import com.example.innotrek.ui.screens.devices.room.RoomViewModel
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@Composable
fun DeviceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    Button(
        onClick = { onClick },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.azul_fondo),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Mostrar datos en Logcat",
            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
        )

    }
}