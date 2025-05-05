package com.example.innotrek.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    navController: NavController,
    coroutineScope: CoroutineScope? = null,
    onItemSelected: () -> Unit = {}
) {
    val drawerItems = listOf(
        AppScreens.HomeScreen to "Inicio",
        AppScreens.MapScreen to "Mapa",
        AppScreens.DeviceScreen to "Dispositivos",
        AppScreens.DeviceConfigScreen to "Configuración Dispositivo",
        AppScreens.TerminalScreen to "Terminal",
        AppScreens.GraphScreen to "Gráficas"
    )

    ModalDrawerSheet {
        Text(
            text = "Menú",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()

        drawerItems.forEach { (screen, label) ->
            val isSelected = navController.currentDestination?.route == screen.route

            NavigationDrawerItem(
                label = { Text(text = label) },
                selected = isSelected,
                onClick = {
                    coroutineScope?.launch {
                        if (!isSelected) {
                            navController.navigate(screen.route) {
                                when {
                                    // Para pantallas principales (excepto configuración)
                                    screen.route != AppScreens.DeviceConfigScreen.route -> {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                    // Para DeviceConfigScreen (navegación simple)
                                    else -> {
                                        // No hacemos popUpTo para permitir volver atrás
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        onItemSelected() // Cierra el drawer y/o ejecuta callback
                    } ?: run {
                        // Manejo alternativo sin CoroutineScope
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                        }
                        onItemSelected()
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}