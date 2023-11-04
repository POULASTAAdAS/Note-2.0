package com.example.note.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToDetailsScreen: (Int) -> Unit,
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

    val allData by homeViewModel.allData.collectAsState()

    val listOfId = homeViewModel.listOfId.collectAsState()
    val listOfIdCount by homeViewModel.listOfIdCount
    val noteEditState by homeViewModel.noteEditState
    val selectAll by homeViewModel.selectAll

    Scaffold(
        topBar = {
            HomeTopBar(
                isData = isData,
                noteEditState = noteEditState,
                selectedNumber = listOfIdCount,
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
    ) { paddingValues ->
        HomeScreenContent(
            paddingValues = paddingValues,
            allData = allData,
            navigateToDetailsScreen = navigateToDetailsScreen,
            selectedNoteId = { id, selectState ->
                if (selectState) {
                    listOfId.value.add(id)
                    homeViewModel.listOfIdCountAdd()
                }
                else {
                    listOfId.value.remove(id)
                    homeViewModel.listOfIdCountMinus()
                }

                if (listOfId.value.isEmpty()) homeViewModel.changeNoteEditState(false)
            },
            noteEditState = noteEditState,
            changeNoteEditState = {
                homeViewModel.changeNoteEditState(true)
            },
            selectAll = selectAll,
            searchOn = false
        )
    }
}