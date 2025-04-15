package com.example.innotrek.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun LoginScreen(navController: NavController,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val imageRatio = 16f / 8f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageRatio)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.salon),
            contentDescription = "Imagen superior interior casa",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(150.dp).padding(bottom = 16.dp)  // Tamaño reducido de la imagen
        )


        Text(
            text = "Log into your account",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Insert your email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Insert your password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null && user.isEmailVerified) {
                                    // Usuario válido y verificado, ir a HomeScreen
                                    navController.navigate("home_screen")
                                } else {
                                    errorMessage = "Por favor verifica tu correo electrónico antes de continuar."
                                    showErrorDialog = true
                                }
                            } else {
                                val exception = task.exception
                                errorMessage = when ((exception as? FirebaseAuthException)?.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> "Este correo no está registrado."
                                    "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta."
                                    "ERROR_INVALID_EMAIL" -> "Correo inválido."
                                    else -> exception?.localizedMessage ?: "Error desconocido"
                                }
                                showErrorDialog = true
                            }
                        }
                } else {
                    errorMessage = "Por favor llena todos los campos."
                    showErrorDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(6, 54, 97),
                contentColor = Color(255, 255, 255)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? ")
            Text(
                "Sign Up",
                color = Color(0xFF3F51B5),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("Registro")
                }
            )
        }
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error al iniciar sesión") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview
@Composable
fun LoginPreview(){
    val navController = rememberNavController()
    LoginScreen(navController)
}
