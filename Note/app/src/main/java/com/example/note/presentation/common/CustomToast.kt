package com.example.note.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomToast(
    text: String,
    icon: ImageVector = Icons.Rounded.Warning,
    color: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                color = color
            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomToast(
        "No Internet connection",
        color = MaterialTheme.colorScheme.inversePrimary
    )
}