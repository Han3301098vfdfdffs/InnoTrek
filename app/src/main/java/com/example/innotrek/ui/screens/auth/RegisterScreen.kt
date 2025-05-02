package com.example.innotrek.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.navigation.AppScreens
import com.example.innotrek.ui.utils.composables.responsiveTextSize
import com.example.innotrek.ui.components.auth.AuthButton
import com.example.innotrek.ui.components.auth.AuthDialog
import com.example.innotrek.ui.components.auth.AuthFooter
import com.example.innotrek.ui.components.auth.AuthHeader
import com.example.innotrek.ui.components.auth.AuthTextField
import com.example.innotrek.ui.components.common.BackButton
import com.example.innotrek.ui.components.common.LogoImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innotrek.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AuthHeader(
                    imageRes = R.drawable.camara,
                    height = if (isLandscape) 360.dp else 160.dp
                )

                LogoImage(height = 150.dp)

                Text(
                    text = "IoT Service App",
                    fontSize = responsiveTextSize(32.sp, 64.sp),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Nuestra app busca brindarle al usuario las herramientas necesarias para que tenga el mejor hogar inteligente, en acorde a sus necesidades.",
                    fontSize = responsiveTextSize(18.sp, 26.sp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create your account",
                    fontSize = responsiveTextSize(18.sp, 26.sp),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = "Insert your name",
                    isLandscape = isLandscape
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = "Insert your email",
                    keyboardType = KeyboardType.Email,
                    isLandscape = isLandscape
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = "Insert your password",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password,
                    isLandscape = isLandscape
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthButton(
                    text = "Sign Up",
                    onClick = {
                        viewModel.register(
                            onSuccess = { /* Puedes manejar navegación aquí si es necesario */ },
                            onError = { /* Manejo de error adicional si es necesario */ }
                        )
                    },
                    isLandscape = isLandscape
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthFooter(
                    text = "Already have an account?",
                    actionText = "LOG IN",
                    onActionClick = { navController.navigate(AppScreens.LoginScreen.route) },
                    isLandscape = isLandscape
                )
            }
        }

        BackButton(
            onClick = { navController.popBackStack() },
            isLandscape = isLandscape
        )
    }

    // Manejo de diálogos
    if (viewModel.showErrorDialog) {
        AuthDialog(
            title = "Error al registrarse",
            message = viewModel.errorMessage,
            onDismiss = { viewModel.showErrorDialog = false }
        )
    }

    if (viewModel.showSuccessDialog) {
        AuthDialog(
            title = "Registro exitoso",
            message = "Tu cuenta se creó correctamente. Antes de iniciar sesión confirma tu correo.",
            onDismiss = {
                viewModel.showSuccessDialog = false
                navController.navigate(AppScreens.LoginScreen.route)
            }
        )
    }
}