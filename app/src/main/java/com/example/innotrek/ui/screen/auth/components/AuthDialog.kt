package com.example.innotrek.ui.screen.auth.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AuthDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    confirmText: String = "OK",
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(confirmText)
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun AuthDialogPreview() {
    AuthDialog(
        title = "Registro exitoso",
        message = "Tu cuenta se creó correctamente.",
        onDismiss = {}
    )
}