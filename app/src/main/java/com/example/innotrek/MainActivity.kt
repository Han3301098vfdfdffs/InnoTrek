package com.example.innotrek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.innotrek.ui.theme.InnoTrekTheme
import com.example.innotrek.ui.theme.Screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            InnoTrekTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    RegisterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
