package com.example.innotrek.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Devices(
    @StringRes val deviceStringResourceId: Int,
    @DrawableRes val deviceDrawableResourceId: Int,
)
