package com.example.note.presentation.common

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.note.R
import com.example.note.ui.theme.forgot_text
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.marker_color
import com.mohamedrejeb.richeditor.model.RichTextState


@Composable
fun TextFieldEditSideComponentButton(
    color1: Color = forgot_text,
    color2: Color = google_login_button,
    icon: Int,
    pressed: Boolean,
    iconPresses: () -> Unit,
    color: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = if (pressed) color1 else color2
    )
) {
    IconButton(
        onClick = iconPresses,
        colors = color
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}

@Composable
fun TextFieldEditSideComponentLinkButton(
    icon: Int,
    pressed: Boolean,
    color: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = if (pressed) forgot_text else google_login_button
    ),
    iconPresses: () -> Unit,
    moveFocus: () -> Unit,
    returnUrl: (String) -> Unit,
) {
    IconButton(
        onClick = iconPresses,
        colors = color
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }

    if (pressed) {
        CustomDialogBox(
            hideDialog = {
                iconPresses()
            },
            clearFocus = moveFocus,
            returnUrl = returnUrl
        )
    }
}


@Composable
fun TextFieldEditSideComponent(
    state: RichTextState,
    bold: Boolean,
    italic: Boolean,
    title: Boolean,
    underLine: Boolean,
    lineThrough: Boolean,
    link: Boolean,
    color: Boolean,
    toggleBold: () -> Unit,
    toggleItalic: () -> Unit,
    toggleTitle: () -> Unit,
    toggleUnderline: () -> Unit,
    toggleLineThrough: () -> Unit,
    toggleLink: () -> Unit,
    toggleColor: () -> Unit,
    moveFocus: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .horizontalScroll(rememberScrollState()),
    ) {
        // ic_bold
        TextFieldEditSideComponentButton(
            icon = R.drawable.ic_bold,
            pressed = bold,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                toggleBold()
            }
        )

        // ic_italic
        TextFieldEditSideComponentButton(
            icon = R.drawable.ic_italic,
            pressed = italic,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontStyle = FontStyle.Italic
                    )
                )
                toggleItalic()
            }
        )

        // ic_title
        val titleSize = MaterialTheme.typography.titleLarge.fontSize
        TextFieldEditSideComponentButton(
            icon = R.drawable.ic_title,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontSize = titleSize
                    )
                )
                toggleTitle()
            },
            pressed = title
        )

        // ic_underline
        TextFieldEditSideComponentButton(
            icon = R.drawable.ic_underline,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.Underline
                    )
                )
                toggleUnderline()
            },
            pressed = underLine
        )

        // ic_lineThrough
        TextFieldEditSideComponentButton(
            icon = R.drawable.ic_linethrough,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.LineThrough
                    )
                )
                toggleLineThrough()
            },
            pressed = lineThrough
        )

        // ic_link
        TextFieldEditSideComponentLinkButton(
            icon = R.drawable.ic_link,
            iconPresses = toggleLink,
            moveFocus = moveFocus,
            returnUrl = {
                state.addLink(
                    text = it,
                    url = it
                )
                toggleLink()
            },
            pressed = link,
        )

        // ic_color
        TextFieldEditSideComponentButton(
            color1 = marker_color.copy(.5f),
            color2 = marker_color,
            icon = R.drawable.ic_color,
            pressed = color,
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        color = marker_color
                    )
                )
                toggleColor()
            }
        )
    }
}


//@Preview
//@Composable
//private fun Preview() {
//    val haptic = LocalHapticFeedback.current
//
//    TextFieldEditSideComponent(
//        haptic = haptic,
//        state = rememberRichTextState(),
//        moveFocus = {}
//    )
//}