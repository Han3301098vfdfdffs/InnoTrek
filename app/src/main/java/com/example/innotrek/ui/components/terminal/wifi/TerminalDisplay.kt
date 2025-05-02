package com.example.innotrek.ui.components.terminal.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black,
        shape = MaterialTheme.shapes.medium
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                Text(
                    text = "> $message",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}