package com.example.innotrek.ui.screens.terminal

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
import com.example.innotrek.ui.screens.common.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun TerminalScreen(navController: NavController) {

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
                    title = "Terminal",
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                BarTerminal()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExampleScreen() {
    // Para preview puedes pasar un NavController falso o null-check dentro
    TerminalScreen(
        navController = rememberNavController(),
        //onSaveDevice = { /* ejemplo */ },
        //onNavigateBack = { /* ejemplo */ }
    )
}