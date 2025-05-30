package com.example.innotrek.ui.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.innotrek.navigation.AppScreens
import com.example.innotrek.ui.utils.responsiveTextSize

@Composable
fun SignUpPrompt(navController: NavController) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "Don't have an account? ",
            fontSize = responsiveTextSize(18.sp, 26.sp)
        )
        Text(
            "Sign Up",
            fontSize = responsiveTextSize(18.sp, 26.sp),
            modifier = Modifier.clickable { navController.navigate(route = AppScreens.RegisterScreen.route) },
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )
    }
}