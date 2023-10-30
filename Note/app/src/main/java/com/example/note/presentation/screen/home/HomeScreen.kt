package com.example.note.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToSelectedScreen: (String) -> Unit
) {
    val allSet by homeViewModel.allSet
    val isData by homeViewModel.isData

    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            HomeTopBar(
                isData = isData,
                noteSelected = false,
                searchIconClicked = {
                    // TODO
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {
                    // TODO
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                clearClicked = {
                    // TODO
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) { // TODO animation
                FloatingNewButton {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigateToSelectedScreen("")
                }
            }
        }
    ) {


        HomeScreenContent(
            paddingValues = it
        )
    }

    LaunchedEffect(key1 = allSet) {
        if (allSet) {
            homeViewModel.getAll()
        }
    }
}