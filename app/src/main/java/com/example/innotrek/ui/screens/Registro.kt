package com.example.innotrek.ui.screens

import androidx.compose.foundation.Image
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
import com.example.innotrek.R

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
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
            onClick = { /* acción del botón */ },
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
                fontWeight = FontWeight.Bold
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
