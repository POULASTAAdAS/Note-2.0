package com.example.note.presentation.screen.recentlyDeleted

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.presentation.common.DefaultIconButton
import com.example.note.ui.theme.background
import com.example.note.ui.theme.google_login_button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyDeletedTopBar(
    dropDownState: Boolean,
    changeDropDownState: () -> Unit,
    navigateBack: () -> Unit,
    deleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Recently Deleted",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                color = MaterialTheme.colorScheme.inversePrimary,
            )
        },
        navigationIcon = {
            DefaultIconButton(
                icon = Icons.Rounded.ArrowBack,
                onClick = navigateBack
            )
        },
        actions = {
            IconButton(
                onClick = changeDropDownState
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )

                DropdownMenu(
                    expanded = dropDownState,
                    onDismissRequest = changeDropDownState
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Delete All")
                        },
                        onClick = deleteAllClicked
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    )
}

@Composable
fun RecentlyDeletedSingleCard(
    noteId: Int,
    heading: String,
    content: String,
    deleteDate: String,
    leftDays: Int,
    recoverClicked: (Int) -> Unit,
    deleteOne: (Int) -> Unit
) {
    val expandState = remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                expandState.value = !expandState.value
            },
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    text = if (leftDays == 1) "$leftDays day" else "$leftDays days",
                    softWrap = false,
                    color = MaterialTheme.colorScheme.inversePrimary.copy(.7f),
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "deleted on: $deleteDate",
                        color = MaterialTheme.colorScheme.inversePrimary.copy(.7f),
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = heading,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    softWrap = false,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.inversePrimary
            )


            AnimatedVisibility(visible = expandState.value) {
                Row {
                    Button(
                        onClick = { recoverClicked(noteId) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Recover",
                            color = google_login_button
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { deleteOne(noteId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Delete",
                                color = google_login_button
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(300.dp),
        modifier = Modifier.padding(20.dp)
    ) {
        RecentlyDeletedSingleCard(
            noteId = 1,
            heading = "heading",
            content = "this is content",
            deleteDate = "2023-10-10",
            leftDays = 1,
            recoverClicked = {},
        ) {

        }
    }
}