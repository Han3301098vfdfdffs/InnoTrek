package com.example.innotrek.ui.screens.devices

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@Composable
fun WifiConnectionFields(
    ipAddress: TextFieldValue,
    port: TextFieldValue,
    onIpChanged: (TextFieldValue) -> Unit,
    onPortChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    Column(modifier = modifier) {
        Text(
            text = "Dirección IP:",
            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
        )
        BasicTextField(
            value = ipAddress,
            onValueChange = onIpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Puerto:",
            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
        )
        BasicTextField(
            value = port,
            onValueChange = onPortChanged,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        )
    }
}