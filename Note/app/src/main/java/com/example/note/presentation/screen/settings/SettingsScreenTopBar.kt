package com.example.note.presentation.screen.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.presentation.common.DefaultIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            )

        },
        navigationIcon = {
            DefaultIconButton(
                onClick = navigateBack,
                icon = Icons.Rounded.ArrowBack
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.inversePrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    )
}


@Preview
@Composable
private fun Preview() {
    SettingsTopBar(navigateBack = {})
}