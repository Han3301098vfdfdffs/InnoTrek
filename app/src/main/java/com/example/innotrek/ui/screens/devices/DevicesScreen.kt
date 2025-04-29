package com.example.innotrek.ui.screens.devices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.innotrek.navigation.NavigationDrawerContent
import com.example.innotrek.ui.components.common.TopAppBar
import com.example.innotrek.ui.components.devices.DeviceContent
import kotlinx.coroutines.launch

@Composable
fun DevicesScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
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
                    title = "Dispositivos",
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                DeviceContent()
            }
        }
    }
}