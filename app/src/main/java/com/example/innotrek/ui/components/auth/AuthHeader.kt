package com.example.innotrek.ui.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.innotrek.R

@Composable
fun AuthHeader(
    imageRes: Int,
    height: Dp,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Auth header image",
            contentScale = contentScale,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun AuthHeaderPreview() {
    AuthHeader(
        imageRes = R.drawable.camara,
        height = 160.dp
    )
}