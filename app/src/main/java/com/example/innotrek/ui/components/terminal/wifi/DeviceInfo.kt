package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.data.WifiConfiguration

@Composable
fun DeviceInfo(
    device: WifiConfiguration?,
    indicatorColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        device?.let { device ->
            Text(
                text = "Tarjeta: ${device.tarjeta}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "IP: ${device.ip}",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Puerto: ${device.puerto}",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
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
        } ?: run {
            Text(
                text = "Ningún dispositivo seleccionado",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.gris_oscuro)
            )
        }
    }
}