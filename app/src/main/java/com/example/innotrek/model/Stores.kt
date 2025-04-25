package com.example.innotrek.model

import com.google.android.gms.maps.model.LatLng

data class Stores(
    val nameResId: Int,  // ID del recurso para el nombre/URL
    val point: LatLng? = null // Opcional, para combinar con los puntos después
)