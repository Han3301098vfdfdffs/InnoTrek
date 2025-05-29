    package com.example.innotrek.ui.screen.deviceconfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innotrek.R
import com.example.innotrek.ui.utils.responsiveTextSize

@Composable
fun ConnectionTypeSelector(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onTypeSelected("wifi") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedType == "wifi") colorResource(id = R.color.azul_fondo) else Color.LightGray,
                contentColor = if (selectedType == "wifi") Color.White else Color.Black
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "WiFi",
                fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { onTypeSelected("bluetooth") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedType == "bluetooth") colorResource(id = R.color.azul_fondo) else Color.LightGray,
                contentColor = if (selectedType == "bluetooth") Color.White else Color.Black
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Bluetooth",
                fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
            )
        }
    }
}