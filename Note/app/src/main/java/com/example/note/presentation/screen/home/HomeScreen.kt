package com.example.note.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToSelectedScreen: (Int) -> Unit,
    navigateToNew: () -> Unit
) {
    val isData by homeViewModel.isData

    val haptic = LocalHapticFeedback.current
    val focsManager = LocalFocusManager.current

    val searchText by homeViewModel.searchText
    val searchEnabled by homeViewModel.searchEnabled

    LaunchedEffect(key1 = Unit) {
        homeViewModel.initialSet()
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                isData = isData,
                noteSelected = false,
                searchEnabled = searchEnabled,
                searchText = searchText,
                searchTextChange = {
                    homeViewModel.changeSearchText(it)
                },
                searchClicked = {
                    homeViewModel.searchClicked()
                    focsManager.clearFocus()
                },
                enableSearch = {
                    homeViewModel.searchIconClicked()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {
                    // TODO
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                clearClicked = {
                    // TODO
                    homeViewModel.clearClicked()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                threeDotClicked = {
                    // TODO
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) { // TODO animation
                FloatingNewButton {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigateToNew()
                }
            }
        }
    ) {
        HomeScreenContent(
            paddingValues = it
        )
    }
}