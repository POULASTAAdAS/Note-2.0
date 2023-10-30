package com.example.note.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.presentation.common.DefaultIconButton
import com.example.note.ui.theme.dark_primary
import com.example.note.ui.theme.google_login_button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isData: Boolean,
    noteSelected: Boolean ,
    searchIconClicked: () -> Unit,
    clearClicked: () -> Unit,
    deleteClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Hi Poulastaa..",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.weight(1f)
                )

                if (isData) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        strokeWidth = 1.5.dp
                    )
                }

                if (noteSelected) {
                    DefaultIconButton(
                        onClick = deleteClicked,
                        icon = Icons.Rounded.Delete
                    )
                }

                if (noteSelected) {
                    DefaultIconButton(
                        onClick = clearClicked,
                        icon = Icons.Rounded.Clear
                    )
                } else {
                    DefaultIconButton(
                        onClick = searchIconClicked
                    )
                }
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    )
}

@Composable
fun FloatingNewButton(
    floatingNewButtonClicked: () -> Unit
) {
    FloatingActionButton(
        onClick = floatingNewButtonClicked,
        containerColor = google_login_button,
        contentColor = dark_primary,
        modifier = Modifier.padding(
            end = 15.dp,
            bottom = 15.dp
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null
        )
    }
}


@Preview
@Composable
private fun Preview() {
    Column {
        HomeTopBar(
            isData = true,
            noteSelected = false,
            searchIconClicked = {},
            deleteClicked = {},
            clearClicked = {}
        )

        FloatingNewButton {

        }
    }
}