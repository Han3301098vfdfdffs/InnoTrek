package com.example.innotrek.ui.screens.maps.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innotrek.R
import com.example.innotrek.data.model.Stores
import com.example.innotrek.ui.utils.responsiveTextSize
import com.example.innotrek.ui.utils.getStoreNameFromResource
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocationCard(
    store: Stores, // Cambiado de location a store
    point: LatLng,
    onClick: () -> Unit,
    onImageClick: () -> Unit,
    isLandscape: Boolean,
    context: Context
) {
    val sizeVerticalFont = 20.sp
    val sizeHorizontalFont = 32.sp

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna para el texto
            Column(modifier = Modifier.weight(0.6f)) {
                Text(
                    text = context.getStoreNameFromResource(store.nameResId), // Usamos nameResId
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                )
                Text(
                    text = if(isLandscape) "Lat: %.12f".format(point.latitude) else "Lat: %.8f".format(point.latitude),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                )
                Text(
                    text = if(isLandscape) "Lng: %.12f".format(point.longitude) else "Lng: %.8f".format(point.longitude),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                )
            }

            // Imagen
            LocationImage(
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(1f),
                imageResId = store.imageResId ?: R.drawable.esp32, // Usamos imageResId
                onClick = onImageClick
            )
        }
    }
}

@Composable
fun LocationImage(
    modifier: Modifier,
    imageResId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Imagen ubicación",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}