package com.example.innotrek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.ui.screens.DeviceConfigScreen
import com.example.innotrek.ui.screens.DevicesScreen
import com.example.innotrek.ui.screens.HomeScreen
import com.example.innotrek.ui.screens.LoginScreen
import com.example.innotrek.ui.screens.MapScreen
import com.example.innotrek.ui.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.DeviceScreen.route
    ){
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.SingUpScreen.route) {
            RegisterScreen(navController)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = AppScreens.MapScreen.route) {
            MapScreen(navController)
        }
        composable(route = AppScreens.DeviceConfigScreen.route) {
            DeviceConfigScreen(navController)
        }
        composable(route = AppScreens.DeviceScreen.route){
            DevicesScreen(navController)
        }
    }
}