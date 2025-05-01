package com.example.innotrek.ui.components.deviceconfig

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.data.model.Device
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeviceImageCarousel(
    devices: List<Device>,
    selectedDeviceIndex: Int,
    onDeviceSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var currentAutoScrollIndex by remember { mutableIntStateOf(0) }
    val autoScrollScope = rememberCoroutineScope()
    var autoScrollJob by remember { mutableStateOf<Job?>(null) }

    // Lógica del carrusel
    LaunchedEffect(selectedDeviceIndex) {
        autoScrollJob?.cancel() // Cancelar cualquier auto-scroll en progreso

        if (selectedDeviceIndex == -1 && devices.isNotEmpty()) {
            // Iniciar auto-scroll solo si no hay dispositivo seleccionado
            autoScrollJob = autoScrollScope.launch {
                while (true) {
                    delay(3000) // 3 segundos entre cambios
                    currentAutoScrollIndex = (currentAutoScrollIndex + 1) % devices.size
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if(isLandscape) 150.dp else 250.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        val displayIndex = if (selectedDeviceIndex != -1) selectedDeviceIndex else currentAutoScrollIndex

        if (devices.isNotEmpty()) {
            Image(
                painter = painterResource(id = devices[displayIndex].deviceDrawableResourceId),
                contentDescription = stringResource(id = devices[displayIndex].deviceStringResourceId),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}