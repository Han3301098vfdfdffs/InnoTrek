package com.example.innotrek.ui.screens.device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.navigation.NavigationDrawerContent
import com.example.innotrek.ui.components.common.TopAppBar
import com.example.innotrek.ui.components.device.AddFab
import com.example.innotrek.ui.components.device.DeviceContent
import kotlinx.coroutines.launch

@Composable
fun DeviceScreen(navController: NavController) {

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
            },
            floatingActionButton = { AddFab(navController)}

        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                DeviceContent(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExampleScreen() {
    // Para preview puedes pasar un NavController falso o null-check dentro
    DeviceScreen(
        navController = rememberNavController(),
        //onSaveDevice = { /* ejemplo */ },
        //onNavigateBack = { /* ejemplo */ }
    )
}