package com.example.innotrek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.innotrek.ui.utils.ResultGoogle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var googleIdToken: String? = null

    fun setGoogleIdToken(token: String) {
        googleIdToken = token
    }

    suspend fun signInWithFirebase(): ResultGoogle<Unit> {
        println("DEBUG: Iniciando autenticación en Firebase")
        return try {
            _loading.value = true
            _errorMessage.value = null

            val token = googleIdToken ?: throw IllegalStateException("No hay token de Google")
            val firebaseCredential = GoogleAuthProvider.getCredential(token, null) // token es el idToken String
            auth.signInWithCredential(firebaseCredential).await()
            println("DEBUG: Autenticación en Firebase exitosa")
            ResultGoogle.Success(Unit)
        } catch (e: Exception) {
            println("DEBUG: Error en Firebase: ${e.message}")
            _errorMessage.value = when (e) {
                is IllegalStateException -> "Error en el proceso de autenticación"
                else -> "Error al iniciar sesión: ${e.message}"
            }
            ResultGoogle.Failure(e)
        } finally {
            _loading.value = false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}