package com.example.note.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    value: String,
    shape: RoundedCornerShape = RoundedCornerShape(40.dp),
    singleLine: Boolean = true,
    label: String,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,
        cursorColor = MaterialTheme.colorScheme.inversePrimary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        unfocusedLabelColor = place_holder,
        focusedLabelColor = MaterialTheme.colorScheme.inversePrimary,
        focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
        unfocusedTrailingIconColor = place_holder
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
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
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
fun SingleCard(
    note: Note,
    noteSelected: Boolean,
    searchOn: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int) -> Unit
) {
    val selected = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    navigateToDetailsScreen(note._id)
                },
                onLongClick = {
                    selected.value = !selected.value
                    selectedNoteId(note._id)
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
//                if (!searchOn) // TODO

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.createDate!!.drop(2),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(end = 8.dp),
                        color = place_holder,
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                    if (noteSelected)
                        if (selected.value)
                            FilledCircle(14.dp)
                        else EmptyCircle()
                    else
                        FilledCircle(4.dp)
                }
            }

            Text(
                text = note.heading!!,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )

            Text(
                text = note.content!!,
                maxLines = if (searchOn) 3 else 6,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleCard(
            note = Note(
                _id = 1,
                heading = "heading",
                content = "Lorem ipsum dolor sit amet, consectetur " +
                        "adipiscing elit. Sed sit amet velit et turpis" +
                        " dictum lacinia a a felis. Sed bibendum arcu a" +
                        " arcu iaculis tincidunt. Sed tempus odio non tellus " +
                        "aliquet, in dapibus quam interdum. Donec quis metus " +
                        "nec ante varius fermentum. Integer euismod, lorem" +
                        " at sodales posuere, sapien justo tempor justo," +
                        " non rhoncus augue nisl nec erat. Nunc eu nisl" +
                        " sit amet ipsum dignissim laoreet. Vestibulum nec" +
                        " magna tincidunt, vestibulum augue eu, sollicitudin" +
                        " metus. Integer tristique vel sapien sit amet bibendum." +
                        " Duis rhoncus odio at eros interdum, a sagittis dui varius." +
                        " Morbi aliquam, dolor id dictum blandit, lectus quam" +
                        " viverra elit, at sagittis risus quam quis felis.",
                createDate = "",
                syncState = false,
            ),
            noteSelected = true,
            searchOn = false,
            navigateToDetailsScreen = {

            },
            selectedNoteId = {

            }
        )
    }
}

//noteId = 1,
//heading = "heading",

//createDate = "10/10/2003",