package com.example.innotrek.data.model

import com.google.android.gms.maps.model.LatLng

data class Point(
    val coordinatesResId: Int, // ID del recurso para las coordenadas
    val coordinates: LatLng? = null // Opcional, para parsear después
)
