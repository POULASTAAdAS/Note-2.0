package com.example.note.presentation.screen.selected

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.note.presentation.screen.data.DataContent
import com.example.note.presentation.screen.data.NoteScreenTopBar
import com.example.note.presentation.screen.home.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedScreen(
    homeViewModel: HomeViewModel,
    navigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val heading by homeViewModel.heading
    val content by homeViewModel.content
    val createDate by homeViewModel.createDate
    val updateDate = homeViewModel.note.value.updateDate.toString()
    val updateTime = homeViewModel.note.value.updateTime.toString()

    Scaffold(
        topBar = {
            NoteScreenTopBar(
                createDate = createDate,
                createTime = "",
                selectedNote = true,
                saveClicked = { _, _ ->
                    homeViewModel.updateSingle()
                    navigateBack()
                },
                cancelClicked = {
                    navigateBack()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {
                    navigateBack()
                    homeViewModel.deleteCalledFromSelectedScreen()
                }
            )
        },
        content = { paddingValues ->
            DataContent(
                focusManager = focusManager,
                paddingValues = paddingValues,
                updateDate = updateDate,
                updateTime = updateTime,
                newNote = false,
                heading = heading,
                onHeadingChange = {
                    homeViewModel.changeHeadingText(it)
                },
                content = content,
                onContentChange = {
                    homeViewModel.changeContentText(it)
                }
            )
        }
    )
}