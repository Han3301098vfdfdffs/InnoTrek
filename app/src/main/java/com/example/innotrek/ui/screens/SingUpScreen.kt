package com.example.innotrek.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.innotrek.R
import com.example.innotrek.navigation.AppScreens
import com.example.innotrek.responsiveTextSize
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if(isLandscape) 360.dp else 160.dp)
                        .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camara),
                        contentDescription = "Encabezado decorativo",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.logoinnoterk),
                    contentDescription = "Logo",
                    modifier = Modifier.height(150.dp).padding(bottom = 16.dp)  // Tamaño reducido de la imagen
                )

                Text(
                    text = "IoT Service App",
                    fontSize = responsiveTextSize(32.sp, 64.sp),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Nuestra app busca brindarle al usuario las herramientas necesarias para que tenga el mejor hogar inteligente, en acorde a sus necesidades.",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Create your account",
                    fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(
                        text = "Insert your name",
                        fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    ) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont))
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
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont))
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if(name.isNotBlank() && email.isNotBlank() && password.isNotBlank()){
                            FirebaseAuth
                                .getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        //Verificar que el email existe
                                        val user = FirebaseAuth.getInstance().currentUser
                                        user?.sendEmailVerification()
                                            ?.addOnCompleteListener { verifyTask ->
                                                if (verifyTask.isSuccessful) {
                                                    //Se verifica y avisa del correo de verificacion
                                                    showSuccessDialog = true
                                                } else {
                                                    errorMessage = "No se pudo enviar el correo de verificación."
                                                    showErrorDialog = true
                                                }
                                            }
                                    }else{
                                        errorMessage = it.exception?.message ?: "Error desconocido"
                                        showErrorDialog = true
                                    }
                                }
                        } else{
                            errorMessage = "Por favor llena todos los campos"
                            showErrorDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.azul_fondo), // Azul (RGB) como color de fondo
                        contentColor = colorResource(id =  R.color.white) // Blanco como color del texto
                    ),
                    modifier = if(isLandscape) Modifier.size(width = 220.dp, height = 70.dp) else Modifier.size(width = 180.dp, height = 50.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Already have an account?",
                        fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    )
                    Text(
                        text = "LOG IN",
                        color = Color(6, 54, 97),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate(route = AppScreens.LoginScreen.route) },
                        fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(75))
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White,
                modifier = Modifier.size(if(isLandscape) 30.dp else 15.dp)
            )
        }
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error al registrarse") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }else{
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Registro exitoso") },
                text = { Text("Tu cuenta se creó correctamente. Antes de iniciar sesión confirma tu correo. Se envio un correo a tu email") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("login_screen")
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

    }
}


@Preview
@Composable
fun RegisterPreview(){
    val navController = rememberNavController()
    RegisterScreen(navController)
}
