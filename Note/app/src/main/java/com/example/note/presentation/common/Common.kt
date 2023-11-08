package com.example.note.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.note.R
import com.example.note.domain.model.Note
import com.example.note.ui.theme.background
import com.example.note.ui.theme.forgot_text
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.non_Sync
import com.example.note.ui.theme.place_holder
import com.example.note.utils.getAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    value: String,
    shape: RoundedCornerShape = RoundedCornerShape(40.dp),
    singleLine: Boolean = true,
    label: String,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        cursorColor = MaterialTheme.colorScheme.inversePrimary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,
        focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
        unfocusedTrailingIconColor = place_holder,
        focusedLabelColor = MaterialTheme.colorScheme.inversePrimary,
        unfocusedLabelColor = place_holder,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Email
    ),
    keyboardActions: KeyboardActions,
    isError: Boolean = false,
    icon: Painter = painterResource(id = R.drawable.ic_email),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    iconClicked: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        label = {
            Text(text = label)
        },
        isError = isError,
        singleLine = singleLine,
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(
                onClick = iconClicked,
            ) { Icon(painter = icon, contentDescription = null) }
        }
    )
}

@Composable
fun DefaultIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Search,
    color: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        contentColor = MaterialTheme.colorScheme.inversePrimary
    ),
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = color,
        modifier = Modifier.then(modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}


@Composable
fun BasicLoginButton(
    basicLoginLoadingState: Boolean,
    loginButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 8.dp
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable {
                loginButtonClicked()
            },
        shape = RoundedCornerShape(40.dp),
        color = MaterialTheme.colorScheme.inversePrimary,
        shadowElevation = 10.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (basicLoginLoadingState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(7.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login",
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
fun ErrorTextFieldIndication(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 8.dp),
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        color = MaterialTheme.colorScheme.error
    )
}


@Composable
fun ForgotText(
    text: String,
    forgotTextClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = text,
        color = forgot_text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource,
            ) {
                forgotTextClicked()
            },
        textAlign = TextAlign.End,
        fontSize = MaterialTheme.typography.titleSmall.fontSize
    )
}


@Composable
fun GoogleLoginButton(
    text: String,
    basicLoginLoadingState: Boolean,
    loadingState: Boolean = false,
    googleButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 8.dp
            )
            .clickable(
                enabled = !basicLoginLoadingState
            ) {
                googleButtonClicked()
            },
        shape = RoundedCornerShape(40.dp),
        shadowElevation = 10.dp,
        color = google_login_button,
        border = BorderStroke(
            width = 2.dp,
            color = Color.DarkGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loadingState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(7.dp),
                    color = MaterialTheme.colorScheme.inversePrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Spacer(modifier = Modifier.weight(.1f))

                Icon(
                    modifier = Modifier
                        .size(35.dp),
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(
                    text = text,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .weight(.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(.1f))
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleCardForGridView(
    note: Note,
    noteEditState: Boolean,
    searchOpen: Boolean,
    selectAll: Boolean,
    changeNoteEditState: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    columnClicked: () -> Unit
) {
    val selected = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = selectAll) {
        selected.value = selectAll
        selectedNoteId(note._id, selected.value)
    }

    LaunchedEffect(key1 = noteEditState) {// click once
        if (!noteEditState)
            if (selected.value)
                selected.value = false
    }

    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = MutableInteractionSource(),
                indication = if (searchOpen) null else LocalIndication.current, // if search enabled then no ripple effect else default
                onClick = {
                    if (searchOpen) columnClicked()
                    else {
                        if (noteEditState) {
                            selected.value = !selected.value
                            selectedNoteId(note._id, selected.value)
                        } else navigateToDetailsScreen(note._id)

                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                },
                onLongClick = {
                    if (!noteEditState) { // one time call
                        changeNoteEditState()
                        selected.value = true
                        selectedNoteId(note._id, selected.value)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            ),
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
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row {
                if (note.pinned)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pin),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = place_holder
                    )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.createDate!!.drop(2),

                        color = place_holder,
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )

                    if (!note.syncState)
                        Spacer(modifier = Modifier.width(8.dp))

                    if (noteEditState)
                        if (selected.value)
                            FilledCircle(14.dp)
                        else EmptyCircle()
                    else
                        if (!note.syncState)
                            FilledCircle(4.dp)
                }
            }

            if (!note.heading.isNullOrEmpty())
                Text(
                    text = note.heading,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )

            Text(
                text = note.content!!,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
        }
    }
}


@Composable
fun SingleCardForResearchResult(
    searchQuery: String,
    noteID: Int,
    heading: String?,
    content: String,
    createDate: String, // TODO change in future
    time: String = "",// TODO add to server database
    navigateToDetailsScreen: (Int) -> Unit, // this will handle other optimization
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToDetailsScreen(noteID)
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
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                if (!heading.isNullOrEmpty())
                    Text(
                        text = getAnnotatedString(
                            text = heading,
                            searchQuery = searchQuery,
                            color = MaterialTheme.colorScheme.error
                        ),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$createDate, $time",
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }

            Text(
                text = getAnnotatedString(
                    text = content,
                    searchQuery = searchQuery,
                    color = MaterialTheme.colorScheme.error
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Light,
            )
        }
    }
}


@Composable
fun EmptyCircle() {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Canvas(
            modifier = Modifier
                .size(14.dp)
        ) {
            drawCircle(
                color = non_Sync,
                style = Stroke(4f)
            )
        }
    }
}

@Composable
fun FilledCircle(
    size: Dp
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Canvas(
            modifier = Modifier
                .size(size)
        ) {
            drawCircle(color = non_Sync)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SingleCardForGridView(
        note = Note(
            heading = "heading",
            content = "this is content",
            pinned = true,
            createDate = "2023-10-10",
            syncState = false
        ),
        noteEditState = false,
        searchOpen = false,
        selectAll = false,
        changeNoteEditState = { /*TODO*/ },
        navigateToDetailsScreen = {},
        selectedNoteId = { _, _ ->

        }
    ) {

    }
}