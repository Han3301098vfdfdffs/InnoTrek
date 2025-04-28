package com.example.innotrek.model

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

data class Stores(
    val nameResId: Int,
    val point: LatLng? = null,
    @DrawableRes val imageResId: Int? = null
)