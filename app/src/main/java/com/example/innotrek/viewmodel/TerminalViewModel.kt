package com.example.innotrek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TerminalViewModel: ViewModel(){
    private val _messages = MutableStateFlow(listOf("Esperando datos..."))
    val messages: StateFlow<List<String>> = _messages

    fun clearTerminal() {
        _messages.value = listOf("Terminal limpiada - ${getCurrentDateTime()}")
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    //Funciones para guardar datos de la terminal
}