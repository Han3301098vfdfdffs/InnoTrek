package com.example.innotrek.ui.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit

//Function Size Font
@Composable
fun responsiveTextSize(portraitSize: TextUnit, landscapeSize: TextUnit): TextUnit {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        landscapeSize else portraitSize
}