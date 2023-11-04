package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.note.domain.model.Note
import com.example.note.presentation.common.SingleCard

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    allData: List<Note>,
    noteEditState: Boolean,
    changeNoteEditState: () -> Unit,
    searchOn: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    selectAll: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
                end = 15.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (allData.isNotEmpty())
            ShowDataAsGrid(
                listOfNote = allData,
                noteEditState = noteEditState,
                changeNoteEditState = changeNoteEditState,
                searchOn = searchOn,
                navigateToDetailsScreen = navigateToDetailsScreen,
                selectedNoteId = selectedNoteId,
                selectAll = selectAll
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
    searchOn: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    selectAll: Boolean
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
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
                searchOn = searchOn,
                navigateToDetailsScreen = navigateToDetailsScreen,
                selectedNoteId = selectedNoteId,
                selectAll = selectAll
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