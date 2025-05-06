package com.example.innotrek.ui.screens.login.components

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun GoogleLoginButton(
    isLandscape: Boolean,
    onSignInSuccess: () -> Unit,
    onSignInFailure: (Exception) -> Unit
    ){

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Estado para controlar si hay un error
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Lanzador para el resultado de la actividad de inicio de sesión de Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, auth, onSignInSuccess, onSignInFailure)
        } catch (e: ApiException) {
            errorMessage = "Error al iniciar sesión con Google: ${e.message}"
            onSignInFailure(e)
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .size(if(isLandscape) 128.dp else 64.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.azul_fondo),
                    shape = RoundedCornerShape(20.dp)
                )
                .background(Color.White)
                .clickable {
                    signInWithGoogle(context , googleSignInLauncher)
                    /*Logica para iniciar sesión con Google*/
                }
                .padding(12.dp), // Espacio interno para que no se pegue
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
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("935999039311-dvs4f7e5kltpcc6sc1s2eg94vctj6boj.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
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