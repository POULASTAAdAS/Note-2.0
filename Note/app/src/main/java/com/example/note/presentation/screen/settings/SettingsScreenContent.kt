package com.example.note.presentation.screen.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.note.R
import com.example.note.presentation.common.LoginTextField
import com.example.note.ui.theme.background
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.place_holder
import com.example.note.utils.getAnnotatedDeleteAccountText

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    context: Context,
    // UserNameChangeCard
    network: Boolean,
    userName: String,
    userNameCardState: Boolean,
    onValueChange: (String) -> Unit,
    changeUserNameCardState: () -> Unit,
    saveClicked: () -> Unit,
    userNameUpdateCancelClicked: () -> Unit,
    // SyncStatusCard
    syncState: Boolean,
    syncText: String,
    syncCardEnabled: Boolean,
    syncCardSwitchClicked: (Boolean) -> Unit,
    // SortTypeCard
    sortType: String,
    updateSortType: (Boolean?) -> Unit,
    // RecentlyDeletedCard
    recentlyDeletedNavigationClick: () -> Unit,
    // logOutCard
    logOutCardState: Boolean,
    logOutCardClicked: () -> Unit,
    logOutConformLogOut: () -> Unit,
    // delete account
    deleteAccountCardState: Boolean,
    deleteAccountCardClicked: () -> Unit,
    deleteAccountConformClicked: () -> Unit,
    isLoggedOut: Boolean,
    isAccountDeleted: Boolean
) {
    val animateBlur by animateDpAsState(
        targetValue = if (isLoggedOut || isAccountDeleted) 5.dp else 0.dp,
        label = "blur effect on screen when search on"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .blur(animateBlur)
            .padding(
                top = paddingValues.calculateTopPadding() + 10.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
                end = 15.dp
            ),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val interactionSource = MutableInteractionSource()

        UserNameChangeCard(
            interactionSource = interactionSource,
            context = context,
            network = network,
            userName = userName,
            userNameCardState = userNameCardState,
            onValueChange = onValueChange,
            changeUserNameCardState = changeUserNameCardState,
            saveClicked = saveClicked,
            userNameUpdateCancelClicked = userNameUpdateCancelClicked
        )

        SyncStatusCard(
            syncState = syncState,
            syncText = syncText,
            syncCardEnabled = syncCardEnabled,
            syncCardSwitchClicked = syncCardSwitchClicked
        )

        SortTypeCard(
            sortType = sortType,
            hideSortTypeCardDropDown = {
                updateSortType(it)
            }
        )

        RecentlyDeletedCard(
            interactionSource = interactionSource,
            recentlyDeletedNavigationClick = recentlyDeletedNavigationClick
        )

        LogoutCard(
            network = network,
            context = context,
            isLoggedOut = isLoggedOut,
            interactionSource = interactionSource,
            cardExpandState = logOutCardState,
            logOutCardClicked = logOutCardClicked,
            logOutConformLogOut = logOutConformLogOut
        )

        DeleteAccountCard(
            network = network,
            context = context,
            isAccountDeleted = isAccountDeleted,
            interactionSource = interactionSource,
            deleteAccountCardState = deleteAccountCardState,
            deleteAccountCardClicked = deleteAccountCardClicked,
            deleteAccountConformClicked = deleteAccountConformClicked
        )
    }
}


@Composable
fun UserNameChangeCard(
    interactionSource: MutableInteractionSource,
    context: Context,
    network: Boolean,
    userName: String,
    userNameCardState: Boolean,
    onValueChange: (String) -> Unit,
    changeUserNameCardState: () -> Unit,
    saveClicked: () -> Unit,
    userNameUpdateCancelClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = changeUserNameCardState
            ),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change Username",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
                AnimatedVisibility(
                    visible = !userNameCardState,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Text(
                        text = "change",
                        color = google_login_button,
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            if (!network) Toast.makeText(
                                context,
                                "Please make sure you have Internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                            else changeUserNameCardState()
                        }
                    )
                }
            }

            val focusManager = LocalFocusManager.current

            AnimatedVisibility(visible = userNameCardState) {
                LoginTextField(
                    value = userName,
                    label = "new username",
                    icon = painterResource(id = R.drawable.ic_user),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            saveClicked()
                            focusManager.clearFocus()
                            changeUserNameCardState()
                        }
                    ),
                    iconClicked = {},
                    onValueChange = onValueChange
                )
            }

            AnimatedVisibility(visible = userNameCardState) {
                Row(

                ) {
                    Button(
                        onClick = {
                            userNameUpdateCancelClicked()
                            changeUserNameCardState()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "cancel",
                            color = google_login_button
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (!network)
                                    Toast.makeText(
                                        context,
                                        "Please make sure you have Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    saveClicked()
                                    changeUserNameCardState()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "save",
                                color = google_login_button
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SyncStatusCard(
    syncState: Boolean,
    syncText: String,
    syncCardEnabled: Boolean,
    syncCardSwitchClicked: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sync",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Switch(
                    checked = syncState,
                    enabled = syncCardEnabled,
                    onCheckedChange = syncCardSwitchClicked,
                    thumbContent = {
                        if (syncState)
                            Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                        else Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                        checkedThumbColor = google_login_button,
                        checkedIconColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Text(
                text = syncText,
                color = place_holder,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun SortTypeCard(
    sortType: String,
    hideSortTypeCardDropDown: (Boolean?) -> Unit // if true Sort by Last Edited else Last Created
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {

        val expanded = remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sort Type",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.inversePrimary,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        expanded.value = !expanded.value
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = sortType,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )

                    SettingsSortTypeDropDown(
                        sortTypeDropDownState = expanded.value,
                        sortType = sortType,
                        hideSortTypeCardDropDown = {
                            hideSortTypeCardDropDown(it)
                            expanded.value = false
                        }
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(
                            if (expanded.value) 180f else 0f
                        )
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            expanded.value = !expanded.value
                        }
                )
            }
        }
    }
}

@Composable
fun SettingsSortTypeDropDown(
    sortTypeDropDownState: Boolean,
    sortType: String,
    hideSortTypeCardDropDown: (Boolean?) -> Unit
) {
    DropdownMenu(
        expanded = sortTypeDropDownState,
        onDismissRequest = { hideSortTypeCardDropDown(null) },
        modifier = Modifier.background(
            color = background.copy(.3f)
        )
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Last Edited")
            },
            trailingIcon = {
                if (sortType == "Last Edited") Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            },
            onClick = {
                hideSortTypeCardDropDown(true)
            },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.inversePrimary,
                trailingIconColor = MaterialTheme.colorScheme.inversePrimary
            )
        )

        DropdownMenuItem(
            text = {
                Text(text = "Last Created")
            },
            trailingIcon = {
                if (sortType == "Last Created") Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            },
            onClick = {
                hideSortTypeCardDropDown(false)
            },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.inversePrimary,
                trailingIconColor = MaterialTheme.colorScheme.inversePrimary
            )
        )
    }
}

@Composable
fun RecentlyDeletedCard(
    interactionSource: MutableInteractionSource,
    recentlyDeletedNavigationClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = recentlyDeletedNavigationClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Recently Deleted Notes",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun LogoutCard(
    network: Boolean,
    context: Context,
    isLoggedOut: Boolean,
    interactionSource: MutableInteractionSource,
    cardExpandState: Boolean,
    logOutCardClicked: () -> Unit,
    logOutConformLogOut: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = logOutCardClicked
            ),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 8.dp,
                end = 8.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LogOut",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inversePrimary
                )


                AnimatedVisibility(visible = isLoggedOut) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            AnimatedVisibility(visible = cardExpandState) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Are you sure you want to logout !\nIf notes are not synced with cloud they will be lost.")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = logOutCardClicked,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.inversePrimary,
                            ),
                        ) {
                            Text(text = "No")
                        }

                        OutlinedButton(
                            onClick = {
                                if (!network)
                                    Toast.makeText(
                                        context,
                                        "Please make sure you have Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else logOutConformLogOut()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.Red
                            )
                        ) {
                            Text(text = "Yes")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DeleteAccountCard(
    network: Boolean,
    context: Context,
    isAccountDeleted: Boolean,
    interactionSource: MutableInteractionSource,
    deleteAccountCardState: Boolean,
    deleteAccountCardClicked: () -> Unit,
    deleteAccountConformClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = deleteAccountCardClicked
            ),
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 8.dp,
                end = 8.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Delete Account")
                        }
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_account),
                    contentDescription = null,
                    tint = Color.Red
                )

                AnimatedVisibility(visible = isAccountDeleted) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            AnimatedVisibility(visible = deleteAccountCardState) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = getAnnotatedDeleteAccountText(Color.Red)
                    )


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = deleteAccountCardClicked,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.inversePrimary,
                            ),
                        ) {
                            Text(text = "No")
                        }

                        OutlinedButton(
                            onClick = {
                                if (!network) {
                                    Toast.makeText(
                                        context,
                                        "Please make sure you have Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else deleteAccountConformClicked()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.Red
                            )
                        ) {
                            Text(text = "Yes")
                        }
                    }
                }
            }
        }
    }
}