package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.note.domain.model.UserExists
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userExists: Boolean,
    homeViewModel: HomeViewModel,
    navigateToSelectedScreen: (String) -> Unit
) {
    Scaffold(
        topBar = {

        },
        floatingActionButton = {

        }
    ) {
        LaunchedEffect(key1 = userExists) {
            delay(400)
            if (userExists) homeViewModel.getAllNoteIfAny() // TODO leave if null
            Log.d("userExists", userExists.toString())
        }

        HomeScreenContent(
            paddingValues = it, test = {
                homeViewModel.getAllNoteIfAny()
            }
        )
    }
}