package com.example.innotrek.ui.screens.maps

import android.content.res.Configuration
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.innotrek.data.DataMaps
import com.example.innotrek.ui.screens.maps.components.MapContent
import com.example.innotrek.navigation.NavigationDrawerContent
import com.example.innotrek.ui.screens.common.TopAppBar
import com.example.innotrek.ui.screens.maps.components.LocationFAB
import com.example.innotrek.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: MapViewModel = viewModel()

    val dataMaps = remember { DataMaps(context) }
    val stores = remember { dataMaps.loadStoresWithPoints() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(stores[0].point ?: LatLng(0.0, 0.0), 10f)
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
                    title = "Tiendas",
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            },
            floatingActionButton = {
                LocationFAB(
                    context = context,
                    viewModel = viewModel,
                    cameraPositionState = cameraPositionState,
                    snackbarHostState = snackbarHostState
                )
            }
        ) { padding ->
            MapContent(
                padding = padding,
                isLandscape = isLandscape,
                stores = stores,
                cameraPositionState = cameraPositionState,
                context = context,
                coroutineScope = scope
            )
        }
    }
}