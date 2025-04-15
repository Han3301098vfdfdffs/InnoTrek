package com.example.innotrek.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object Registro: AppScreens("registro")
    object SplashScreen: AppScreens("splash_screen")
    object HomeScreen: AppScreens("home_screen")
}