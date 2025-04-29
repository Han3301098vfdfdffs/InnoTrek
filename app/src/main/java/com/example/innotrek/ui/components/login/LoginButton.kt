package com.example.innotrek.ui.components.login

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innotrek.R
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@Composable
fun LoginButton(
    onClick: () -> Unit,
    isLandscape: Boolean
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.azul_fondo),
            contentColor = colorResource(id = R.color.white)
        ),
        modifier = if(isLandscape) Modifier.size(width = 220.dp, height = 70.dp) else Modifier.size(width = 180.dp, height = 50.dp)
    ) {
        Text(text = "Log In",
            fontSize = responsiveTextSize(18.sp, 26.sp)
        )
    }
}