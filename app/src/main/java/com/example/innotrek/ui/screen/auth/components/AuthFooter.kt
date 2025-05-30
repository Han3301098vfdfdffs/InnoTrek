package com.example.innotrek.ui.screen.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innotrek.R

@Composable
fun AuthFooter(
    text: String,
    actionText: String,
    onActionClick: () -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val fontSize = if (isLandscape) 26.sp else 18.sp

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = fontSize
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = actionText,
            color = colorResource(id = R.color.azul_texto),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onActionClick),
            fontSize = fontSize
        )
    }
}

@Preview
@Composable
fun AuthFooterPreview() {
    AuthFooter(
        text = "Already have an account?",
        actionText = "LOG IN",
        onActionClick = {},
        isLandscape = false
    )
}