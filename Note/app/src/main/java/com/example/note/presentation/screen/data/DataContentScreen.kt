package com.example.note.presentation.screen.data

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.presentation.common.TextFieldEditSideComponent
import com.example.note.ui.theme.place_holder
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataContent(
    haptic: HapticFeedback,
    focusManager: FocusManager,
    state: RichTextState,
    paddingValues: PaddingValues,
    heading: String,
    newNote: Boolean,
    onHeadingChange: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        if (newNote) focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DataScreenTextField(
            text = heading,
            onTextChange = onHeadingChange,
            placeHolder = "heading..",
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Row {
            RichTextEditor( // content
                state = state,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.ime)
                    .weight(.8f),
                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.inversePrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
                    placeholderColor = place_holder
                ),
                placeholder = {
                    Text(text = "write something...")
                }
            )

            val show = remember {
                mutableStateOf(false)
            }

            Column {
                IconButton(
                    onClick = {
                        show.value = !show.value
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    modifier = Modifier.rotate(if (show.value) 180f else 0f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }

                AnimatedVisibility(visible = show.value) {
                    TextFieldEditSideComponent(
                        haptic = haptic,
                        state = state,
                        moveFocus = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    val state = rememberRichTextState()

    val haptic = LocalHapticFeedback.current

    val focusManager = LocalFocusManager.current

    DataContent(
        haptic = haptic,
        state = state,
        focusManager = focusManager,
        paddingValues = PaddingValues(),
        heading = "",
        newNote = true,
        onHeadingChange = {},
    )
}