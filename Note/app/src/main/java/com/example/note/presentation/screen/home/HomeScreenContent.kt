package com.example.note.presentation.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.domain.model.Note
import com.example.note.presentation.common.SingleCard
import com.example.note.ui.theme.primary

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    allData: List<Note>,
    navigateToDetailsScreen: (Int) -> Unit
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
            AnimatedVisibility(visible = true) {
                ShowDataAsGrid(
                    listOfNote = allData,
                    noteSelected = false,
                    searchOn = false,
                    navigateToDetailsScreen = navigateToDetailsScreen,
                    selectedNoteId = {

                    }
                )
            }
        else
            Log.d("data", allData.toString())
    }
}


@Composable
fun ShowDataAsGrid(
    listOfNote: List<Note>,
    noteSelected: Boolean,
    searchOn: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 10.dp , bottom = 10.dp),
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
                noteSelected = noteSelected,
                searchOn = searchOn,
                navigateToDetailsScreen = navigateToDetailsScreen,
                selectedNoteId = selectedNoteId
            )
        }
    }
}