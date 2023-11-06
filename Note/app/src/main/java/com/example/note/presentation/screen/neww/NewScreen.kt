package com.example.note.presentation.screen.neww

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.note.presentation.screen.data.DataContent
import com.example.note.presentation.screen.data.NoteScreenTopBar
import com.example.note.presentation.screen.home.HomeViewModel
import com.example.note.utils.getCurrentDtTime
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun NewScreen(
    homeViewModel: HomeViewModel,
    navigateBack: () -> Unit
) {
    val heading by homeViewModel.heading

    val state = rememberRichTextState()

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current
    val content = LocalContext.current

    val localDtTime = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.clearHeading()
        localDtTime.value = getCurrentDtTime()
    }

    Scaffold(
        topBar = {
            NoteScreenTopBar(
                timeStamp = localDtTime.value,
                saveClicked = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    focusManager.clearFocus()

                    if (state.toMarkdown().trim().isEmpty() && heading.isEmpty()) Toast.makeText(
                        content,
                        "nothing to save",
                        Toast.LENGTH_SHORT
                    ).show()
                    else homeViewModel.getContentFromRichTextField(state.toMarkdown())

                    navigateBack()
                },
                cancelClicked = {
                    navigateBack()
                    focusManager.clearFocus()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {}
            )
        },
        content = { paddingValues ->
            DataContent(
                haptic = haptic,
                state = state,
                focusManager = focusManager,
                paddingValues = paddingValues,
                heading = heading,
                newNote = true,
                onHeadingChange = {
                    homeViewModel.changeHeadingText(it)
                }
            )
        }
    )
}