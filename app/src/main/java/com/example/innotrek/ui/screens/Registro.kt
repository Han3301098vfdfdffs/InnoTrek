package com.example.innotrek.ui.screens

import androidx.appcompat.app.AlertDialog
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

@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
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
    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(150.dp).padding(bottom = 16.dp)  // Tamaño reducido de la imagen
        )

        Text("IoT Service App", fontSize = 32.sp, fontWeight = FontWeight.Bold,  modifier = Modifier.align(Alignment.Start))
        Text("Nuestra app busca brindarle al usuario las herramientas necesarias para que tenga el mejor hogar inteligente, en acorde a sus necesidades."
                ,fontSize = 18.sp, modifier = Modifier.align(Alignment.Start)  )
        Spacer(modifier = Modifier.height(10.dp))
        Text("Create your account", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Insert your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Insert your email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Insert your password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                containerColor = Color(6, 54, 97), // Azul (RGB) como color de fondo
                contentColor = Color(255, 255, 255) // Blanco como color del texto
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Text("Already have an account? ")
            Text(
                "LOG IN",
                color = Color(6, 54, 97),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login_screen") }
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
