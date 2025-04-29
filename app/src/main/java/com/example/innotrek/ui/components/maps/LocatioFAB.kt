import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.example.innotrek.ui.screens.maps.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LocationFAB(
    context: Context,
    viewModel: MapViewModel = viewModel(),
    cameraPositionState: CameraPositionState,
    snackbarHostState: SnackbarHostState
) {
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                if (viewModel.hasLocationPermission(context)) {
                    viewModel.getLastLocation(
                        fusedLocationClient = fusedLocationClient,
                        onSuccess = { latLng ->
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                )
                            }
                        },
                        onFailure = { e ->
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Error al obtener ubicación: ${e.message}",
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Permiso de ubicación denegado",
                        withDismissAction = true
                    )
                }
            }
        }
    )

    FloatingActionButton(
        onClick = {
            viewModel.requestLocationPermission(
                context = context,
                onPermissionGranted = {
                    viewModel.getLastLocation(
                        fusedLocationClient = fusedLocationClient,
                        onSuccess = { latLng ->
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                )
                            }
                        },
                        onFailure = { e ->
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Error de seguridad al acceder a la ubicación",
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                },
                onPermissionDenied = {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                onShowRationale = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Se necesita permiso de ubicación para esta función",
                            withDismissAction = true
                        )
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            )
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