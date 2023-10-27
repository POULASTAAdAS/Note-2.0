package com.example.note.presentation.screen.home

import android.app.Activity
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.note.domain.model.UserExists
import com.example.note.presentation.common.signUp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToSelectedScreen: (String) -> Unit
) {
    Scaffold(
        topBar = {

        },
        floatingActionButton = {

        }
    ) {
        HomeScreenContent(
            paddingValues = it,
            test = {
                homeViewModel.temp()
            }
        )
    }
}