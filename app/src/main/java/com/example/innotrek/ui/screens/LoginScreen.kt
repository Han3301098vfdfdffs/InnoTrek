package com.example.innotrek.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.innotrek.R
import com.example.innotrek.navigation.AppScreens
import com.example.innotrek.responsiveTextSize
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun LoginScreen(navController: NavController) {
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //Autenticación Email
    val auth = FirebaseAuth.getInstance()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    //Variables obtenidos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val imageRatio = 16f / 8f


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {

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
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logoinnoterk),
                contentDescription = "Logo",
                modifier = Modifier.height(150.dp).padding(bottom = 16.dp)  // Tamaño reducido de la imagen
            )

            Text(
                text = "Log into your account",
                fontSize = responsiveTextSize(sizeVerticalFont,sizeHorizontalFont),
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(
                    text = "Insert your email",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                textStyle = TextStyle(fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont))
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(
                    text = "Insert your password",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    ) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                textStyle = TextStyle(fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont))
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
                                        navController.navigate(route = AppScreens.HomeScreen.route)
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
                    containerColor = colorResource(id = R.color.azul_fondo),
                    contentColor = colorResource(id = R.color.white)
                ),
                modifier = if(isLandscape) Modifier.size(width = 220.dp, height = 70.dp) else Modifier.size(width = 180.dp, height = 50.dp)
            ) {
                Text(text = "Log In",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                )
                Text(
                    "Sign Up",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont),
                    color = colorResource(id = R.color.azul_texto),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(route = AppScreens.SingUpScreen.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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


@Composable
fun LoginScreenPreviewOnly() {
    val imageRatio = 16f / 8f
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(id = R.drawable.logoinnoterk),
            contentDescription = "Logo",
            modifier = Modifier.height(150.dp).padding(bottom = 16.dp)  // Tamaño reducido de la imagen
        )


        Text(
            text = "Log into your account",
            fontSize = responsiveTextSize(18.sp, 22.sp),
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
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.azul_fondo),
                contentColor = colorResource(id = R.color.white)
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
                color = colorResource(id = R.color.azul_texto),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {

                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.azul_fondo),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(Color.White)
                    .clickable {}
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    LoginScreenPreviewOnly()
}

