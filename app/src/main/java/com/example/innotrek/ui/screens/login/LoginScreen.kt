package com.example.innotrek.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.innotrek.ui.screens.login.components.LoginHeader
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.ui.screens.common.LogoImage
import com.example.innotrek.ui.screens.login.components.EmailField
import com.example.innotrek.ui.screens.login.components.ErrorDialog
import com.example.innotrek.ui.screens.login.components.GoogleLoginButton
import com.example.innotrek.ui.screens.login.components.LoginButton
import com.example.innotrek.ui.screens.login.components.PasswordField
import com.example.innotrek.ui.screens.login.components.SignUpPrompt
import com.example.innotrek.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LoginHeader(imageRatio = 16f/8f)
            LogoImage(height = 150.dp)

            EmailField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
            )
            Spacer(modifier = Modifier.height(16.dp))

            LoginButton(
                onClick = { viewModel.login(navController) },
                isLandscape = isLandscape
            )
            Spacer(modifier = Modifier.height(8.dp))

            GoogleLoginButton(isLandscape = isLandscape)

            Spacer(modifier = Modifier.height(16.dp))

            SignUpPrompt(navController)
        }
    }

    if (viewModel.showErrorDialog) {
        ErrorDialog(
            message = viewModel.errorMessage,
            onDismiss = { viewModel.showErrorDialog = false }
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreen(){
    val navController = rememberNavController()
    LoginScreen(navController)
}