package com.example.note.presentation.common

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.R
import com.example.note.ui.theme.dark_primary
import com.example.note.ui.theme.forgot_text
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.marker_color
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState


@Composable
fun TextFieldEditSideComponentButton(
    icon: Painter,
    presses: MutableState<Boolean> = remember { mutableStateOf(false) },
    color: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = if (presses.value) forgot_text else google_login_button
    ),
    iconPresses: (Boolean) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    IconButton(
        onClick = {
            presses.value = !presses.value
            iconPresses(presses.value)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        colors = color
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}


@Composable
fun TextFieldEditSideComponent(
    state: RichTextState
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        // ic_bold
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_bold
            ),
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        )

        // ic_italic
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_italic
            ),
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        )

        val titleSize = MaterialTheme.typography.titleLarge.fontSize

        // ic_title
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_title
            ),
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        fontSize = titleSize
                    )
                )
            }
        )

        // ic_underline
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_underline
            ),
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        )

        // ic_lineThrough
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_linethrough
            ),
            iconPresses = {
                state.toggleSpanStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }
        )

        //
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_link
            ),
            iconPresses = {

            }
        )



        val colorEnable = remember { mutableStateOf(false) }
        // ic_color
        TextFieldEditSideComponentButton(
            icon = painterResource(
                id = R.drawable.ic_color
            ),
            iconPresses = {
                colorEnable.value = it
                state.toggleSpanStyle(
                    SpanStyle(
                        color = marker_color
                    )
                )
            },
            color = IconButtonDefaults.filledIconButtonColors(
                contentColor = dark_primary,
                containerColor = if (colorEnable.value) marker_color.copy(.5f) else marker_color
            ),
        )
    }
}


@Preview
@Composable
private fun Preview() {
    TextFieldEditSideComponent(
        state = rememberRichTextState()
    )

//    ColorPicker(color = Color.Red)
}