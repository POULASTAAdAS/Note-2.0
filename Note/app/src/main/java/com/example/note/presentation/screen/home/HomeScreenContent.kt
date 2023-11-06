package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import com.example.note.domain.model.Note
import com.example.note.presentation.common.SingleCard

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    allData: List<Note>,
    noteEditState: Boolean,
    searchEnabled: Boolean,
    selectAll: Boolean,
    changeNoteEditState: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    columnClicked: () -> Unit
) {
    val animateBlur by animateDpAsState(
        targetValue = if (searchEnabled) 2.dp else 0.dp,
        label = "blur effect on screen when search on"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .blur(radius = animateBlur)
            .clickable(
                enabled = searchEnabled,
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = columnClicked
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (allData.isNotEmpty())
            ShowDataAsGrid(
                listOfNote = allData,
                noteEditState = noteEditState,
                changeNoteEditState = changeNoteEditState,
                searchEnabled = searchEnabled,
                navigateToDetailsScreen = {
                    navigateToDetailsScreen(it)
                },
                selectedNoteId = { it1, it2 ->
                    selectedNoteId(it1, it2)
                },
                selectAll = selectAll,
                columnClicked = columnClicked
            )
        else
            Log.d("data", allData.toString())
    }
}


@Composable
fun ShowDataAsGrid(
    listOfNote: List<Note>,
    noteEditState: Boolean,
    changeNoteEditState: () -> Unit,
    searchEnabled: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    selectAll: Boolean,
    columnClicked: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        userScrollEnabled = !searchEnabled,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 10.dp,
            start = 15.dp,
            end = 15.dp
        ),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
    ) {
        items(
            items = listOfNote,
            key = {
                it._id
            }
        ) {
            SingleCard(
                note = it,
                noteEditState = noteEditState,
                changeNoteEditState = changeNoteEditState,
                searchEnabled = searchEnabled,
                navigateToDetailsScreen = navigateToDetailsScreen,
                selectedNoteId = selectedNoteId,
                selectAll = selectAll,
                columnClicked = columnClicked
            )
        }
    }
}


//@Composable
//fun ShowSearchResult(
//    listOfNote: List<Note>,
//    noteSelected: Boolean,
//    searchOn: Boolean,
//    navigateToDetailsScreen: (Int) -> Unit,
//) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(10.dp),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        items(
//            items = listOfNote,
//            key = {
//                it._id
//            }
//        ) {
//            SingleCard(
//                note = it,
//                noteEditState = noteSelected,
//                searchOn = searchOn,
//                navigateToDetailsScreen = navigateToDetailsScreen,
//                selectedNoteId = { _, _ ->
//
//                }
//            )
//        }
//    }
//}