package com.example.innotrek.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    onClick: () -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
            .background(
                color = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(75)
            )
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(if (isLandscape) 30.dp else 15.dp)
        )
    }
}

@Preview
@Composable
fun BackButtonPreview() {
    BackButton(
        onClick = {},
        isLandscape = false
    )
}