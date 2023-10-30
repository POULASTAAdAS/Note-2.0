package com.example.note.presentation.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.ui.theme.primary

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = primary // TODO change
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

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreviewLight() {
    Scaffold(
        modifier = Modifier.background(
            color = primary
        ),
        topBar = {
            HomeTopBar(
                isData = false,
                noteSelected = false,
                enableSearch = {
                },
                deleteClicked = {
                },
                clearClicked = {
                },
                searchEnabled = true,
                searchText = "",
                searchTextChange = {

                }, searchClicked = {},
                threeDotClicked = {}
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) { // TODO animation
                FloatingNewButton {
                }
            }
        }
    ) {
        HomeScreenContent(
            paddingValues = it
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    Scaffold(
        modifier = Modifier.background(
            color = primary
        ),
        topBar = {
            HomeTopBar(
                isData = false,
                noteSelected = false,
                enableSearch = {
                },
                deleteClicked = {
                },
                clearClicked = {
                },
                searchEnabled = true,
                searchText = "",
                searchTextChange = {

                },
                searchClicked = {},
                threeDotClicked = {}
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) { // TODO animation
                FloatingNewButton {
                }
            }
        }
    ) {
        HomeScreenContent(
            paddingValues = it
        )
    }
}