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

@Composable
fun NavigationDrawerContent(
    navController: NavController,
    onItemSelected: () -> Unit = {}  // Callback al seleccionar un ítem
) {
    val items = listOf(
        AppScreens.HomeScreen to "Inicio",
        AppScreens.MapScreen to "Mapa",
        AppScreens.DeviceScreen to "Configuración Dispositivo"
    )

    ModalDrawerSheet {
        Text("Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        HorizontalDivider()

        items.forEach { (screen, label) ->
            val isCurrentDestination = navController.currentDestination?.route == screen.route

            NavigationDrawerItem(
                label = { Text(label) },
                selected = isCurrentDestination,
                onClick = {
                    if (!isCurrentDestination) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    onItemSelected()  // Siempre cierra el Drawer (tanto para ítem actual como otros)
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}