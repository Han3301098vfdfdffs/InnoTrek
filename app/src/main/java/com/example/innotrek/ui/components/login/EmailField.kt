package com.example.innotrek.ui.components.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isLandscape: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(
            text = "Insert your email",
            fontSize = responsiveTextSize(18.sp,26.sp)
        ) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = responsiveTextSize(18.sp, 26.sp))

    )
}