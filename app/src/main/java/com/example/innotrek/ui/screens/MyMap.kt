package com.example.innotrek.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import com.example.innotrek.data.DataMaps
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun MyMap() {
    var hasLocationPermission by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }


    val stores = remember { DataMaps().LoadStores() }
    val points = remember { DataMaps().LoadPoints() }

    lifecycle.addObserver(rememberMapLifeCycle(mapView))

    Column(modifier = Modifier.fillMaxSize()) {
        // Header (Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(4, 139, 171)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* acción botón menú */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mapa de Tiendas",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // Mapa
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                shadowElevation = 8.dp
            ) {
                AndroidView(
                    factory = {
                        mapView.apply {
                            getMapAsync { gMap ->
                                googleMap = gMap

                                val defaultLocation = LatLng(19.4603, -99.1456) // CDMX
                                val zoomLevel = 15f

                                gMap.uiSettings.isZoomControlsEnabled = true
                                gMap.uiSettings.isZoomGesturesEnabled = true
                                gMap.uiSettings.isMyLocationButtonEnabled = false

                                if (hasLocationPermission) {
                                    try {
                                        gMap.isMyLocationEnabled = true
                                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                            if (location != null) {
                                                val currentLatLng = LatLng(location.latitude, location.longitude)
                                                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                                            }
                                        }
                                    } catch (e: SecurityException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, zoomLevel))
                                }

                                //Posición
                                stores.zip(points).forEach { (store, point) ->
                                    val link = context.getString(store.stringResourceId)
                                    val positionString = context.getString(point.stringResourcesId)
                                    val latLng = positionString.toLatLng()

                                    val marker = gMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title("")//Titulo
                                    )
                                    marker?.tag = link
                                }

                                //Marcador
                                gMap.setOnMarkerClickListener { marker ->
                                    val url = marker.tag as? String
                                    url?.let {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                        try {
                                            intent.setPackage("com.google.android.apps.maps")
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                            context.startActivity(fallbackIntent)
                                        }
                                    }
                                    true
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            //Boton Ubicacion
            FloatingActionButton(
                onClick = {
                    if (hasLocationPermission) {
                        try {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null && googleMap != null) {
                                    val currentLatLng = LatLng(location.latitude, location.longitude)
                                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                                }
                            }
                        } catch (e: SecurityException) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Mi ubicación"
                )
            }
        }
    }
}

@Composable
fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        onResult(granted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
                arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        )
    }
}

@Composable
fun rememberMapLifeCycle(mapView: MapView): LifecycleObserver {
    return remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
    }
}

fun String.toLatLng(): LatLng {
    val parts = this.split(",")
    val lat = parts[0].toDoubleOrNull() ?: 0.0
    val lng = parts[1].toDoubleOrNull() ?: 0.0
    return LatLng(lat, lng)
}
