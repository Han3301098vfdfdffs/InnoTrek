package com.example.innotrek.ui.screens.login.components


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

@Composable
fun GoogleLoginButton(
    isLandscape: Boolean,
    onSignInSuccess: () -> Unit,
    onSignInFailure: (Exception) -> Unit
) {
    val context = LocalContext.current
    val auth = Firebase.auth

    // Nuevo: Usar el cliente de Identity Services
    val oneTapClient = remember { Identity.getSignInClient(context) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val googleIdToken = credential.googleIdToken
            when {
                googleIdToken != null -> {
                    firebaseAuthWithGoogle(googleIdToken, auth, onSignInSuccess, onSignInFailure)
                }
                else -> onSignInFailure(Exception("No se encontró token de Google"))
            }
        } catch (e: Exception) {
            onSignInFailure(e)
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(if (isLandscape) 128.dp else 64.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.azul_fondo),
                    shape = RoundedCornerShape(20.dp)
                )
                .background(Color.White)
                .clickable { signInWithGoogle(oneTapClient, googleSignInLauncher) }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icons8_logo_de_google_240),
                contentDescription = "Sign in with Google",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun signInWithGoogle(
    oneTapClient: SignInClient,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("935999039311-dvs4f7e5kltpcc6sc1s2eg94vctj6boj.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            launcher.launch(
                IntentSenderRequest.Builder(
                    result.pendingIntent.intentSender
                ).build()
            )
        }
        .addOnFailureListener { exception ->
            // Manejar error
        }
}

private fun firebaseAuthWithGoogle(
    idToken: String,
    auth: FirebaseAuth,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                task.exception?.let(onFailure)
            }
        }
}