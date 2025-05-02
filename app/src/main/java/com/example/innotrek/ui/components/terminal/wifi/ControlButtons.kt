package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R

@Composable
fun ControlButtons(
    onSelectDeviceClicked: () -> Unit,
    onConnectClicked: () -> Unit,
    isConnectEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = onSelectDeviceClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = colorResource(id = R.color.white)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar\nDispositivo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onConnectClicked,
            enabled = isConnectEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.verde_exito),
                contentColor = colorResource(id = R.color.white)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Conectar")
        }
    }
}