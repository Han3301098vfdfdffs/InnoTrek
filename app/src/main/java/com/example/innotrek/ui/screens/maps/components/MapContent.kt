package com.example.innotrek.ui.screens.maps.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.innotrek.data.model.Stores
import com.example.innotrek.ui.utils.openMapsUrl
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.CoroutineScope

@Composable
fun MapContent(
    padding: PaddingValues,
    isLandscape: Boolean,
    stores: List<Stores>, // Cambiado a List<Stores>
    cameraPositionState: CameraPositionState,
    context: Context,
    coroutineScope: CoroutineScope
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Mapa
            StoreMap(
                modifier = Modifier.weight(if(isLandscape) 0.5f else 0.7f),
                cameraPositionState = cameraPositionState,
                stores = stores, // Cambiado a stores
                context = context
            )

            // Lista de ubicaciones
            LocationList(
                modifier = Modifier.weight(0.3f),
                stores = stores, // Cambiado a stores
                cameraPositionState = cameraPositionState,
                isLandscape = isLandscape,
                context = context,
                coroutineScope = coroutineScope
            )
        }
    }
}

@Composable
fun StoreMap(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    stores: List<Stores>, // Cambiado a List<Stores>
    context: Context
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(/* igual */)
    ) {
        stores.forEach { store ->
            store.point?.let { point ->
                Marker(
                    state = MarkerState(position = point),
                    title = context.getString(store.nameResId), // Usamos nameResId directamente
                    snippet = "Ver en Maps",
                    onClick = {
                        val url = context.getString(store.nameResId)
                        openMapsUrl(context, url)
                        true
                    }
                )
            }
        }
    }
}