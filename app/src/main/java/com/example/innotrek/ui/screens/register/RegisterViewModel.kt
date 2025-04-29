package com.example.innotrek.ui.screens.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var showErrorDialog by mutableStateOf(false)
    var showSuccessDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    private val auth = FirebaseAuth.getInstance()

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor llena todos los campos"
            showErrorDialog = true
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                showSuccessDialog = true
                                onSuccess()
                            } else {
                                errorMessage = "No se pudo enviar el correo de verificación."
                                showErrorDialog = true
                                onError(errorMessage)
                            }
                        }
                } else {
                    errorMessage = task.exception?.message ?: "Error desconocido"
                    showErrorDialog = true
                    onError(errorMessage)
                }
            }
    }
}