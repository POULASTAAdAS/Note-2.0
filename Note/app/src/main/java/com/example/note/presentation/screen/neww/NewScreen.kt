package com.example.note.presentation.screen.neww

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.note.presentation.screen.data.DataContent
import com.example.note.presentation.screen.home.HomeViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun NewScreen(
    homeViewModel: HomeViewModel,
) {
    val heading by homeViewModel.heading
    val content by homeViewModel.content

    val state = rememberRichTextState()

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = Unit) {
        homeViewModel.clearHeading()
    }

    Scaffold(
        topBar = {
            // TODO take content
        },
        content = { paddingValues ->
            DataContent(
                haptic = haptic,
                state = state,
                paddingValues = paddingValues,
                heading = heading,
                content = content,
                onHeadingChange = {
                    homeViewModel.changeHeadingText(it)
                }
            )
        }
    )
}