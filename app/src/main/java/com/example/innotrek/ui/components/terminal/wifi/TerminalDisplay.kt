package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TerminalDisplay(
    messages: List<String>,
    scrollState: ScrollState,
) {
    Text(
        text = "Terminal",
        style = MaterialTheme.typography.titleMedium
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray),
        color = Color.Black,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState),
        ) {
            messages.forEach { message ->
                Text(
                    text = message,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}