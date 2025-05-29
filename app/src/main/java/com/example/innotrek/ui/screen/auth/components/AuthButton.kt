package com.example.innotrek.ui.screen.auth.components

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

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val fontSize = if (isLandscape) 26.sp else 18.sp

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.azul_fondo),
            contentColor = colorResource(id = R.color.white)
        ),
        modifier = modifier.size(
            width = if (isLandscape) 220.dp else 180.dp,
            height = if (isLandscape) 70.dp else 50.dp
        )
    ) {
        Text(text, fontSize = fontSize)
    }
}