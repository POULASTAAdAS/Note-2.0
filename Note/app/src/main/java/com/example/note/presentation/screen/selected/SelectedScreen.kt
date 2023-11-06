package com.example.note.presentation.screen.selected

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.note.presentation.screen.data.DataContent
import com.example.note.presentation.screen.data.NoteScreenTopBar
import com.example.note.presentation.screen.home.HomeViewModel
import com.mohamedrejeb.richeditor.model.rememberRichTextState


@Composable
fun SelectedScreen(
    homeViewModel: HomeViewModel,
    navigateBack: () -> Unit
) {
    val state = rememberRichTextState()

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val heading by homeViewModel.heading
    val content by homeViewModel.content
    val createDate by homeViewModel.createDate

    LaunchedEffect(key1 = content) {
        state.setMarkdown(content)
    }

    Scaffold(
        topBar = {
            NoteScreenTopBar(
                createTime = createDate,
                selectedNote = true,
                saveClicked = { _ ->
                    homeViewModel.updateSingle(content = state.toMarkdown())
                    navigateBack()
                },
                cancelClicked = navigateBack,
                deleteClicked = {
                    // TODO delete operation
                    navigateBack()
                }
            )
        },
        content = { paddingValues ->
            DataContent(
                haptic = haptic,
                focusManager = focusManager,
                state = state,
                paddingValues = paddingValues,
                heading = heading,
                newNote = false,
                onHeadingChange = {
                    homeViewModel.changeHeadingText(it)
                }
            )
        }
    )
}