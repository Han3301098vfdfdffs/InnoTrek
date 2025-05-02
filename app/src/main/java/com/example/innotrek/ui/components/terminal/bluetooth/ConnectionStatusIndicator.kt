package com.example.innotrek.ui.components.terminal.bluetooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.viewmodel.BluetoothViewModel

@Composable
fun ConnectionStatusIndicator(
    connectionState: BluetoothViewModel.ConnectionState,
    modifier: Modifier = Modifier
) {
    val indicatorColor = when (connectionState) {
        is BluetoothViewModel.ConnectionState.Connected -> colorResource(id = R.color.verde_exito)
        is BluetoothViewModel.ConnectionState.Error -> colorResource(id = R.color.rojo_error)
        is BluetoothViewModel.ConnectionState.Connecting -> colorResource(id = R.color.amarillo_conectando)
        else -> colorResource(id = R.color.gris_desactivado)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "Estado:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(end = 4.dp)
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )
    }
}
