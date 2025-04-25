package com.example.innotrek.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object SingUpScreen: AppScreens("singup_screen")
    object SplashScreen: AppScreens("splash_screen")
    object HomeScreen: AppScreens("home_screen")
    object MapScreen: AppScreens("map_screen")
    object DeviceConfigScreen: AppScreens("device_config_screen")

    // Función para evitar rutas mal escritas
    companion object {
        fun fromRoute(route: String?): AppScreens = when (route) {
            SplashScreen.route -> SplashScreen
            LoginScreen.route -> LoginScreen
            SingUpScreen.route -> SingUpScreen
            HomeScreen.route -> HomeScreen
            MapScreen.route -> MapScreen
            DeviceConfigScreen.route -> DeviceConfigScreen

            null -> HomeScreen  // Ruta por defecto
            else -> throw IllegalArgumentException("Ruta '$route' no reconocida")
        }
    }
}