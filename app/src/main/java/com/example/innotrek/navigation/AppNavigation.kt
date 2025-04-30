package com.example.innotrek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.ui.screens.maps.MapScreen
import com.example.innotrek.ui.screens.devices.DevicesScreen
import com.example.innotrek.ui.screens.login.LoginScreen
import com.example.innotrek.ui.screens.home.HomeScreen
import com.example.innotrek.ui.screens.auth.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ){
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
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
        composable(route = AppScreens.DeviceScreen.route){
            DevicesScreen(navController)
        }
    }
}