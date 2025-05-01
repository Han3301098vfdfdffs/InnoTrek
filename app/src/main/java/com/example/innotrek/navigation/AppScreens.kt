package com.example.innotrek.navigation

import com.example.innotrek.ui.screens.terminal.TerminalScreen

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")
    object SplashScreen: AppScreens("splash_screen")
    object HomeScreen: AppScreens("home_screen")
    object MapScreen: AppScreens("map_screen")
    object DeviceConfigScreen: AppScreens("deviceconfig_screen")
    object DeviceScreen: AppScreens("device_screen")
    object TerminalScreen: AppScreens("terminal_screen")

    // Función para evitar rutas mal escritas
    companion object {
        fun fromRoute(route: String?): AppScreens = when (route) {
            SplashScreen.route -> SplashScreen
            LoginScreen.route -> LoginScreen
            RegisterScreen.route -> RegisterScreen
            HomeScreen.route -> HomeScreen
            MapScreen.route -> MapScreen
            DeviceConfigScreen.route -> DeviceConfigScreen
            DeviceScreen.route -> DeviceScreen
            TerminalScreen.route -> TerminalScreen

            null -> HomeScreen  // Ruta por defecto
            else -> throw IllegalArgumentException("Ruta '$route' no reconocida")
        }
    }
}