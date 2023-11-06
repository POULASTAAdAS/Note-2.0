package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.launch

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
                selectAll = selectAll,
                selectAllClicked = {
                    homeViewModel.selectAll()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                searchTextChange = {
                    homeViewModel.changeSearchText(it)
                },
                searchClicked = {
                    focsManager.clearFocus()
                },
                enableSearch = {
                    homeViewModel.searchIconClicked()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {
                    // TODO get list of id from listOfId and perform database operation
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                clearClicked = {
                    homeViewModel.clearClicked()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                threeDotClicked = {
                    // TODO crate settings screen and selectAll button and pin and new idea
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) { // TODO animate floating action button like wp
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
            searchEnabled = searchEnabled,
            selectAll = selectAll,
            noteEditState = noteEditState,
            navigateToDetailsScreen = {
                navigateToDetailsScreen(it)
            },
            selectedNoteId = { id, selectState ->
                homeViewModel.handleAddOrRemoveOfId(id, selectState)
                if (listOfId.value.isEmpty()) homeViewModel.changeNoteEditState(false)
            },
            changeNoteEditState = {
                homeViewModel.changeNoteEditState(true)
            },
            columnClicked = {
                focsManager.clearFocus()
                homeViewModel.searchIconClicked()
            }
        )
    }
}