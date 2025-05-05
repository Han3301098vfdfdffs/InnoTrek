package com.example.innotrek.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.innotrek.ui.screens.common.TopAppBar
import com.example.innotrek.ui.screens.home.components.HomeContent
import com.example.innotrek.ui.screens.home.components.HomeDrawer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    // Estado del drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    HomeDrawer(
        drawerState = drawerState,
        navController = navController,
        coroutineScope = scope
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = "InnoTrek", // Texto del título
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->
            HomeContent(
                modifier = Modifier.padding(padding)
            ) {
                // Aquí iría el contenido real de tu pantalla
                Text("Contenido principal")
            }
        }
    }
}