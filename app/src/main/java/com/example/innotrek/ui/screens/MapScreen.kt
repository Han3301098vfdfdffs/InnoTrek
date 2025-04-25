package com.example.innotrek.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.data.DataMaps
import com.example.innotrek.navigation.NavigationDrawerContent
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido
            } else {
                // Permiso denegado
            }
        }
    )

    // Cliente para obtener la ubicación
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Mover cameraPositionState al ámbito correcto (fuera del Box)
    val dataMaps = remember { DataMaps(context) }
    val locations = remember { dataMaps.loadStoresWithPoints() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(locations[0].point ?: LatLng(0.0, 0.0), 10f)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                onItemSelected = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("InnoTrek") },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.azul_fondo),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { location: Location? ->
                                    location?.let {
                                        val userLatLng = LatLng(it.latitude, it.longitude)
                                        scope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    userLatLng,
                                                    15f
                                                )
                                            )
                                        }
                                    }
                                }
                        } else {
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    },
                    containerColor = colorResource(id = R.color.azul_fondo),
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Centrar en mi ubicación",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Mapa más grande (70% de la pantalla)
                    GoogleMap(
                        modifier = Modifier.weight(0.7f),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = true,
                            scrollGesturesEnabled = true,
                            // Añade estas líneas para evitar conflictos con el Drawer:
                            zoomGesturesEnabled = true,
                            scrollGesturesEnabledDuringRotateOrZoom = false
                        )
                    ) {
                        locations.forEach { location ->
                            location.point?.let { point ->
                                Marker(
                                    state = MarkerState(position = point),
                                    title = context.getString(location.nameResId),
                                    snippet = "Ver en Maps",
                                    onClick = {
                                        val url = context.getString(location.nameResId)
                                        openMapsUrl(context, url)
                                        true
                                    }
                                )
                            }
                        }
                    }

                    // Lista de ubicaciones (30% restante)
                    Column(
                        modifier = Modifier
                            .weight(0.3f)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        locations.forEach { location ->
                            location.point?.let { point ->
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    onClick = {
                                        scope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(point, 15f)
                                            )
                                        }
                                    }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = context.getStoreNameFromResource(location.nameResId),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Lat: ${point.latitude}, Lng: ${point.longitude}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Context.getStoreNameFromResource(resId: Int): String {
    return this.resources.getResourceEntryName(resId)
        .removePrefix("Point_")
        .replace("_", " ")
}

private fun openMapsUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}