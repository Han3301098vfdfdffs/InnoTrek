package com.example.innotrek.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.innotrek.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var showErrorDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun login(navController: NavController) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor llena todos los campos."
            showErrorDialog = true
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        navController.navigate(route = AppScreens.HomeScreen.route)
                    } else {
                        errorMessage = "Por favor verifica tu correo electrónico..."
                        showErrorDialog = true
                    }
                } else {
                    handleLoginError(task.exception)
                }
            }
    }

    private fun handleLoginError(exception: Exception?) {
        errorMessage = when ((exception as? FirebaseAuthException)?.errorCode) {
            "ERROR_USER_NOT_FOUND" -> "Este correo no está registrado."
            "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta."
            "ERROR_INVALID_EMAIL" -> "Correo inválido."
            else -> exception?.localizedMessage ?: "Error desconocido"
        }
        showErrorDialog = true
    }
}