package com.example.note.presentation.screen.recentlyDeleted

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.note.R
import com.example.note.domain.model.RecentlyDeletedNotes
import com.example.note.presentation.common.CustomToast
import com.example.note.presentation.screen.empty.EmptyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyDeletedScreen(
    recentlyDeletedViewModel: RecentlyDeletedViewModel,
    navigateBack: () -> Unit
) {
    val recentlyDeletedNote by recentlyDeletedViewModel.recentlyDeletedNotes.collectAsState()
    val dropDownState by recentlyDeletedViewModel.dropDownState

    Scaffold(
        topBar = {
            RecentlyDeletedTopBar(
                dropDownState = dropDownState,
                changeDropDownState = {
                    recentlyDeletedViewModel.changeDropDownState()
                },
                deleteAllClicked = {
                    recentlyDeletedViewModel.deleteAllClicked()
                },
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        RecentlyDeletedContent(
            recentlyDeletedNote = recentlyDeletedNote,
            paddingValues,
            recoverClicked = { id ->
                recentlyDeletedViewModel.recoverOne(id)
            },
            deleteOne = { id ->
                recentlyDeletedViewModel.deleteOne(id)
            }
        )
    }
}

@Composable
fun RecentlyDeletedContent(
    recentlyDeletedNote: List<RecentlyDeletedNotes>,
    paddingValues: PaddingValues,
    recoverClicked: (Int) -> Unit,
    deleteOne: (Int) -> Unit
) {
    val showToast = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        showToast.value = true
    }

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
    ) {
        AnimatedVisibility(
            visible = showToast.value,
            enter = fadeIn(
                animationSpec = tween(1_000)
            ) + expandVertically(
                animationSpec = tween(1_000)
            )
        ) {
            CustomToast(text = "The number of days represents the days left until it is permanently deleted")
        }

        if (recentlyDeletedNote.isNotEmpty())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = recentlyDeletedNote,
                    key = {
                        it.id
                    }
                ) {
                    RecentlyDeletedSingleCard(
                        noteId = it.id,
                        heading = it.heading ?: "",
                        content = it.content ?: "",
                        deleteDate = it.deleteDate,
                        leftDays = it.leftDays,
                        recoverClicked = recoverClicked,
                        deleteOne = deleteOne
                    )
                }
            }
        else EmptyScreen(lottie = R.raw.empty_recently_deleted_screen)
    }
}