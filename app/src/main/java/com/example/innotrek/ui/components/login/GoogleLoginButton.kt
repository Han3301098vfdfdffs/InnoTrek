package com.example.innotrek.ui.components.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.innotrek.R

@Composable
fun GoogleLoginButton(isLandscape: Boolean){
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