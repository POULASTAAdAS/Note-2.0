package com.example.note.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToDetailsScreen: (Int) -> Unit,
    navigateToNew: () -> Unit
) {
    val showCircularProgressIndicator by homeViewModel.showCircularProgressIndicator

    val customToast by homeViewModel.showCustomToast

    val haptic = LocalHapticFeedback.current
    val focsManager = LocalFocusManager.current

    val searchText by homeViewModel.searchText

    val searchOpen by homeViewModel.searchOpen
    val searchTriggered by homeViewModel.searchTriggered

    val network = homeViewModel.network.value

    LaunchedEffect(key1 = Unit) {
        homeViewModel.initialSet()
        homeViewModel.clearTextFields() // clearing this from newScreen or selectedScreen will clear text if them is changed
    }

    LaunchedEffect(key1 = network) {
        homeViewModel.showCustomToast()
    }

    val allData by homeViewModel.allData.collectAsState()
    val searchResult by homeViewModel.searchResult.collectAsState()

    val listOfId = homeViewModel.listOfId.collectAsState()
    val listOfIdCount by homeViewModel.listOfIdCount

    val noteEditState by homeViewModel.noteEditState
    val selectAll by homeViewModel.selectAll

    Scaffold(
        topBar = {
            HomeTopBar(
                showCircularProgressIndicator = showCircularProgressIndicator,
                noteEditState = noteEditState,
                selectedNumber = listOfIdCount,
                searchOpen = searchOpen,
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
                    focsManager.clearFocus() // database search is triggered from onValueChange
                },
                enableSearch = {
//                    homeViewModel.searchIconClicked()
                    homeViewModel.temp()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                deleteClicked = {
                    homeViewModel.deleteCalledFromHomeScreen()
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
            val floatingNewButton = remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = Unit) {
                floatingNewButton.value = true
            }


            AnimatedVisibility(
                visible = floatingNewButton.value,
                enter = fadeIn(
                    animationSpec = tween(800)
                ) + slideInHorizontally(
                    animationSpec = tween(1000),
                    initialOffsetX = {
                        it / 2
                    }
                )
            ) {
                FloatingNewButton {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigateToNew()
                }
            }
        }
    ) { paddingValues ->
        HomeScreenContent(
            showCustomToast = customToast,
            searchQuery = searchText,
            paddingValues = paddingValues,
            allData = allData,
            searchResult = searchResult,
            searchOpen = searchOpen,
            searchTriggered = searchTriggered,
            selectAll = selectAll,
            noteEditState = noteEditState,
            navigateToDetailsScreen = {
                navigateToDetailsScreen(it)
                homeViewModel.navigatedToDetailsScreenCleanUp()
            },
            selectedNoteId = { id, selectState ->
                homeViewModel.handleAddOrRemoveOfIdFromListOfId(id, selectState)
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