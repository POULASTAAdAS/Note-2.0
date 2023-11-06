package com.example.note.presentation.screen.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor() : ViewModel() {
    val bold = mutableStateOf(false)
    val italic = mutableStateOf(false)
    val title = mutableStateOf(false)
    val underline = mutableStateOf(false)
    val lineThrough = mutableStateOf(false)
    val link = mutableStateOf(false)
    val color = mutableStateOf(false)

}