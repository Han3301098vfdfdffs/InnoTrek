package com.example.innotrek.ui.screens.maps.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.innotrek.data.model.Stores
import com.example.innotrek.ui.utils.openMapsUrlPublic
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LocationList(
    modifier: Modifier,
    stores: List<Stores>, // Cambiado a List<Stores>
    cameraPositionState: CameraPositionState,
    isLandscape: Boolean,
    context: Context,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        stores.forEach { store ->
            store.point?.let { point ->
                LocationCard(
                    store = store, // Cambiado de location a store
                    point = point,
                    onClick = {
                        // Usamos el scope proporcionado
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(point, 15f)
                            )
                        }
                    },
                    onImageClick = {
                        // Usamos la versión pública de openMapsUrl
                        openMapsUrlPublic(context, context.getString(store.nameResId))
                    },
                    isLandscape = isLandscape,
                    context = context
                )
            }
        }
    }
}