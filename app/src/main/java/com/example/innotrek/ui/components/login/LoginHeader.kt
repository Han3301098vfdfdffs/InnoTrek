package com.example.innotrek.ui.components.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R

@Composable
fun LoginHeader(imageRatio: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageRatio)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.salon),
            contentDescription = "Imagen superior interior casa",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        )
    }
}


