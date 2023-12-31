package com.example.note.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.example.note.R
import com.example.note.domain.model.Note
import com.example.note.presentation.common.CustomToastInternet
import com.example.note.presentation.common.SingleCardForGridView
import com.example.note.presentation.common.SingleCardForSesearchResult
import com.example.note.presentation.screen.empty.EmptyScreen

@Composable
fun HomeScreenContent(
    customToastState: Boolean,
    searchQuery: String,
    paddingValues: PaddingValues,
    allData: List<Note>,
    searchResult: List<Note>,
    noteEditState: Boolean,
    searchOpen: Boolean,
    searchTriggered: Boolean,
    selectAll: Boolean,
    changeNoteEditState: () -> Unit,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    columnClicked: () -> Unit,
) {
    val animateBlur by animateDpAsState(
        targetValue = if (searchOpen && !searchTriggered) 2.dp else 0.dp,
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
                enabled = searchOpen && !searchTriggered,
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = columnClicked
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = customToastState,
            enter = fadeIn(
                animationSpec = tween(1_000)
            ) + expandVertically(
                animationSpec = tween(1_000)
            ),
            exit = fadeOut(
                animationSpec = tween(1_000)
            ) + shrinkVertically(
                animationSpec = tween(durationMillis = 1_000)
            )
        ) {
            val toast = remember(this) { customToastState }

            if (toast)
                CustomToastInternet()
        }

        if (allData.isNotEmpty())
            if (searchTriggered)
                ShowSearchResult(
                    searchQuery = searchQuery,
                    listOfNote = searchResult,
                    navigateToDetailsScreen = navigateToDetailsScreen
                )
            else
                ShowDataAsGrid(
                    listOfNote = allData,
                    noteEditState = noteEditState,
                    changeNoteEditState = changeNoteEditState,
                    searchOpen = searchOpen,
                    navigateToDetailsScreen = navigateToDetailsScreen,
                    selectedNoteId = { id, selectState ->
                        selectedNoteId(id, selectState)
                    },
                    selectAll = selectAll,
                    columnClicked = columnClicked
                )
        else
            if (searchTriggered) EmptyScreen(lottie = R.raw.no_search_result)
            else EmptyScreen(lottie = R.raw.lest_go)
    }
}


@Composable
fun ShowDataAsGrid(
    listOfNote: List<Note>,
    noteEditState: Boolean,
    changeNoteEditState: () -> Unit,
    searchOpen: Boolean,
    navigateToDetailsScreen: (Int) -> Unit,
    selectedNoteId: (Int, Boolean) -> Unit,
    selectAll: Boolean,
    columnClicked: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize(),
        userScrollEnabled = !searchOpen,
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
            SingleCardForGridView(
                note = it,
                noteEditState = noteEditState,
                changeNoteEditState = changeNoteEditState,
                searchOpen = searchOpen,
                navigateToDetailsScreen = navigateToDetailsScreen,
                selectedNoteId = selectedNoteId,
                selectAll = selectAll,
                columnClicked = columnClicked
            )
        }
    }
}


@Composable
fun ShowSearchResult(
    listOfNote: List<Note>,
    searchQuery: String,
    navigateToDetailsScreen: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = listOfNote,
            key = {
                it._id
            }
        ) {
            SingleCardForSesearchResult(
                searchQuery = searchQuery,
                noteID = it._id,
                heading = it.heading,
                content = it.content!!,
                createDate = it.createDate!!.drop(2),
                navigateToDetailsScreen = navigateToDetailsScreen
            )
        }
    }
}