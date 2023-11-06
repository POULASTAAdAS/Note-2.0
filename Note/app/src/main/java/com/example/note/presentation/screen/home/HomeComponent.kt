package com.example.note.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.presentation.common.DefaultIconButton
import com.example.note.ui.theme.dark_primary
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.non_Sync
import com.example.note.ui.theme.place_holder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String = "Poulastaa", // todo get from email as apiResponse when user loges in
    isData: Boolean,
    selectAll: Boolean,
    noteEditState: Boolean,
    selectedNumber: Int,
    searchOpen: Boolean,
    searchText: String,
    selectAllClicked: () -> Unit,
    searchTextChange: (String) -> Unit,
    searchClicked: () -> Unit,
    enableSearch: () -> Unit,
    clearClicked: () -> Unit,
    deleteClicked: () -> Unit,
    threeDotClicked: () -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = searchOpen) {
        if (searchOpen) focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            if (searchOpen)
                TextField(
                    value = searchText,
                    onValueChange = searchTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = clearClicked) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = null
                            )
                        }
                    },
                    placeholder = {
                        Text(text = "Search...")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchClicked()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.inversePrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
                        unfocusedPlaceholderColor = place_holder,
                        focusedPlaceholderColor = place_holder
                    ),
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                )
            else
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (noteEditState) "  $selectedNumber" else "Hi $userName...",
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        modifier = Modifier.weight(1f)
                    )

                    if (isData) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            strokeWidth = 1.5.dp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    if (noteEditState) {
                        DefaultIconButton(
                            onClick = selectAllClicked,
                            icon = Icons.Rounded.Check,
                            color = IconButtonDefaults.iconButtonColors(
                                contentColor = if (selectAll) non_Sync
                                else MaterialTheme.colorScheme.inversePrimary
                            )
                        )

                        DefaultIconButton(
                            onClick = deleteClicked,
                            icon = Icons.Rounded.Delete
                        )
                    } else
                        DefaultIconButton(
                            onClick = enableSearch
                        )

                    DefaultIconButton(
                        onClick = threeDotClicked,
                        icon = Icons.Rounded.MoreVert
                    )
                }
        },
        navigationIcon = {
            if (noteEditState)
                DefaultIconButton(
                    onClick = clearClicked,
                    icon = Icons.Rounded.ArrowBack
                )
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
            isData = false,
            noteEditState = false,
            enableSearch = {},
            deleteClicked = {},
            clearClicked = {},
            searchOpen = false,
            selectAll = false,
            searchText = "",
            searchTextChange = {

            },
            searchClicked = {},
            threeDotClicked = {},
            selectedNumber = 5,
            selectAllClicked = {}
        )

        FloatingNewButton {

        }
    }
}