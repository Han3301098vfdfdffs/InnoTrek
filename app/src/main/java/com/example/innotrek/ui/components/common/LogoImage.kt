package com.example.innotrek.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.innotrek.R

@Composable
fun LogoImage(
    height: Dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.logoinnoterk),
        contentDescription = "App logo",
        modifier = modifier
            .height(height)
            .padding(bottom = 16.dp)
    )
}

@Preview
@Composable
fun LogoImagePreview() {
    LogoImage(height = 150.dp)
}