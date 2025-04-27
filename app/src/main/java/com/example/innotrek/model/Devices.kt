package com.example.innotrek.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Devices(
    @StringRes val stringResourceId: Int,
    @DrawableRes val drawableResourceId: Int,
)
