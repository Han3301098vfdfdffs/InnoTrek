package com.example.innotrek.ui.screens.maps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    fun requestLocationPermission(
        context: Context,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        onShowRationale: () -> Unit
    ) {
        when {
            hasLocationPermission(context) -> onPermissionGranted()
            shouldShowRationale(context) -> onShowRationale()
            else -> onPermissionDenied()
        }
    }

    fun getLastLocation(
        fusedLocationClient: FusedLocationProviderClient,
        onSuccess: (LatLng) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        viewModelScope.launch {
                            onSuccess(userLatLng)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    viewModelScope.launch {
                        onFailure(e)
                    }
                }
        } catch (e: SecurityException) {
            viewModelScope.launch {
                onFailure(e)
            }
        }
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRationale(context: Context): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}