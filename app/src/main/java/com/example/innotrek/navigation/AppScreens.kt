package com.example.innotrek.navigation


sealed class AppScreens(val route: String) {
    data object LoginScreen: AppScreens("login_screen")
    data object RegisterScreen: AppScreens("register_screen")
    data object HomeScreen: AppScreens("home_screen")
    data object MapScreen: AppScreens("map_screen")
    data object DeviceConfigScreen: AppScreens("deviceconfig_screen")
    data object DeviceScreen: AppScreens("device_screen")
    data object TerminalScreen: AppScreens("terminal_screen")
}