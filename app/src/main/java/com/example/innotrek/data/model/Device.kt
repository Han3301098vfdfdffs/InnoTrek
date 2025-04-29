package com.example.innotrek.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Device(
    @StringRes val deviceStringResourceId: Int,
    @DrawableRes val deviceDrawableResourceId: Int,
)
