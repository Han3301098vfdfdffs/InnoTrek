package com.example.innotrek.ui.screen.login.components


import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.ui.utils.GoogleAuthHelper
import com.example.innotrek.ui.utils.ResultGoogle
import com.example.innotrek.viewmodel.GoogleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GoogleLoginButton(
    navController: NavController,
    viewModel: GoogleViewModel = viewModel(),
    onSuccess: () -> Unit = { navController.navigate("home_screen") }
) {
    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(Color.White)
                .clickable(
                    enabled = !loading,
                    onClick = {
                        if (context !is Activity) {
                            Toast.makeText(context, "Se requiere una Activity", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        coroutineScope.launch(Dispatchers.IO) {
                            val googleAuthHelper = GoogleAuthHelper(context)

                            when (val result = googleAuthHelper.signInWithGoogle(
                                clientId = context.getString(R.string.default_web_client_id)
                            )) {
                                is ResultGoogle.Success -> {
                                    val token = result.value // <- Aquí faltaba completar la línea
                                    viewModel.setGoogleIdToken(token)
                                    when (val authResult = viewModel.signInWithFirebase()) {
                                        is ResultGoogle.Success -> {
                                            withContext(Dispatchers.Main) { // Vuelve a Main para navegar
                                                onSuccess()
                                            }
                                        }
                                        is ResultGoogle.Failure -> {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Error en Firebase: ${authResult.exception.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                                is ResultGoogle.Failure -> {
                                    withContext(Dispatchers.Main) {
                                        val errorMsg = when (result.exception) {
                                            is NoCredentialException -> "Selección de cuenta cancelada"
                                            else -> "Error en Google: ${result.exception.message}"
                                        }
                                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                    }

                                }
                            }
                        }
                    }
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.icons8_logo_de_google_240),
                    contentDescription = "Sign in with Google",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}