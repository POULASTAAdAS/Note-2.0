package com.example.note.presentation.screen.neww

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.note.utils.getCurrentDate
import com.example.note.utils.getCurrentTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScreen(
    homeViewModel: HomeViewModel,
    navigateBack: () -> Unit
) {
    val heading by homeViewModel.heading
    val content by homeViewModel.content

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val createTime = remember {
        mutableStateOf("")
    }
    val localDate = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        localDate.value = getCurrentDate().toString()
        createTime.value = getCurrentTime()
    }

    Scaffold(
        topBar = {
            NoteScreenTopBar(
                createDate = localDate.value,
                createTime = createTime.value,
                saveClicked = { date, time ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    focusManager.clearFocus()

                    if (content.trim().isNotEmpty() || heading.trim().isNotEmpty())
                        homeViewModel.addAndPushToServer(date, time)
                    else
                        Toast.makeText(
                            context,
                            "nothing to save",
                            Toast.LENGTH_SHORT
                        ).show()

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
                focusManager = focusManager,
                paddingValues = paddingValues,
                heading = heading,
                content = content,
                newNote = true,
                onHeadingChange = {
                    homeViewModel.changeHeadingText(it)
                },
                onContentChange = {
                    homeViewModel.changeContentText(it)
                }
            )
        }
    )
}