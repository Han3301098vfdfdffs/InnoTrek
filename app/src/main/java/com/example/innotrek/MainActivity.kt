package com.example.innotrek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innotrek.navigation.AppNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {

    private val  viewModel by viewModels<SplashModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Splash dura n segundos
        installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                    viewModel.isLoading.value
                }
            }

        setContent {
            AppNavigation()
        }
    }
}

class SplashModel: ViewModel(){
    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init{
        viewModelScope.launch {
            delay(1.seconds)
            _isLoading.value = false
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppNavigation()
}