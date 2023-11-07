package com.example.note.presentation.screen.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.presentation.common.DefaultIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopBar(
    createTime: String = "",
    selectedNote: Boolean = false,
    saveClicked: (String) -> Unit,
    cancelClicked: () -> Unit,
    deleteClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = createTime,
                    modifier = Modifier.weight(.8f),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    textAlign = TextAlign.Center
                )

                if (selectedNote)
                    DefaultIconButton(
                        onClick = deleteClicked,
                        modifier = Modifier.weight(.12f),
                        icon = Icons.Rounded.Delete
                    )

                DefaultIconButton(
                    onClick = { saveClicked(createTime) },
                    modifier = Modifier.weight(.12f),
                    icon = Icons.Rounded.Check
                )
            }
        },
        navigationIcon = {
            DefaultIconButton(
                onClick = cancelClicked,
                icon = Icons.Rounded.Clear
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.inversePrimary.copy(.5f),
        )
    )
}

@Preview
@Composable
private fun Preview() {
    NoteScreenTopBar(
        saveClicked = {},
        cancelClicked = {},
        deleteClicked = {}
    )
}