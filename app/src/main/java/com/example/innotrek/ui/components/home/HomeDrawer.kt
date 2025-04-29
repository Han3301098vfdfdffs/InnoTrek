package com.example.innotrek.ui.components.home

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.innotrek.navigation.NavigationDrawerContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeDrawer(
    drawerState: DrawerState,
    navController: NavController,
    coroutineScope: CoroutineScope,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                onItemSelected = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}