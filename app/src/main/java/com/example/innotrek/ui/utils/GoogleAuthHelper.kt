package com.example.innotrek.ui.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

class GoogleAuthHelper(private val context: Context) {

    suspend fun signInWithGoogle(clientId: String): ResultGoogle<String> {
        Log.d("GoogleAuth", "Iniciando flujo de Google")
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            Log.d("GoogleAuth", "Solicitando credenciales")

            val response = credentialManager.getCredential(
                request = request,
                context = context // Asegúrate de que sea una Activity
            )
            Log.d("GoogleAuth", "Respuesta recibida")
            handleCredentialResponse(response)
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Error específico: ${e.javaClass.simpleName}", e)
            return handleCredentialException(e)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error general", e)
            ResultGoogle.Failure(e)
        }
    }

    private fun handleCredentialResponse(
        response: GetCredentialResponse
    ): ResultGoogle<String> {
        return when (val credential = response.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                        ResultGoogle.Success(googleIdToken.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("GoogleAuth", "Error parseando token", e)
                        ResultGoogle.Failure(e)
                    }
                } else {
                    Log.e("GoogleAuth", "Tipo de credencial no soportado: ${credential.type}")
                    ResultGoogle.Failure(Exception("Tipo de credencial no soportado"))
                }
            }
            else -> {
                Log.e("GoogleAuth", "Tipo de credencial no esperado: ${credential::class.java.simpleName}")
                ResultGoogle.Failure(Exception("Tipo de credencial no esperado"))
            }
        }
    }

    private fun handleCredentialException(e: GetCredentialException): ResultGoogle<String> {
        return when (e) {
            is NoCredentialException -> {
                Log.w("GoogleAuth", "Usuario canceló la selección de cuenta")
                ResultGoogle.Failure(Exception("No se seleccionó una cuenta"))
            }
            else -> {
                Log.e("GoogleAuth", "Error de CredentialManager: ${e.javaClass.simpleName}", e)
                ResultGoogle.Failure(Exception("Error de autenticación: ${e.message}"))
            }
        }
    }
}