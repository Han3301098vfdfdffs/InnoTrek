package com.example.innotrek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.ui.screen.maps.MapScreen
import com.example.innotrek.ui.screen.deviceconfig.DeviceConfigScreen
import com.example.innotrek.ui.screen.login.LoginScreen
import com.example.innotrek.ui.screen.home.HomeScreen
import com.example.innotrek.ui.screen.auth.RegisterScreen
import com.example.innotrek.ui.screen.device.DeviceScreen
import com.example.innotrek.ui.screen.terminal.TerminalScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route // ← Solo cambia esto
    ) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController) // Pasa el navController
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = AppScreens.MapScreen.route) {
            MapScreen(navController)
        }
        composable(route = AppScreens.DeviceScreen.route) {
            DeviceScreen(navController)
        }
        composable(route = AppScreens.DeviceConfigScreen.route) {
            DeviceConfigScreen(navController)
        }
        composable(route = AppScreens.TerminalScreen.route) {
            TerminalScreen(navController)
        }
    }
}
