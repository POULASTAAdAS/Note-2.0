package com.example.note.presentation.screen.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor() : ViewModel() {
    val bold = mutableStateOf(false)

    fun toggleBold() {
        bold.value = !bold.value
    }

    val italic = mutableStateOf(false)

    fun toggleItalic() {
        italic.value = !italic.value
    }

    val title = mutableStateOf(false)

    fun toggleTitle() {
        title.value = !title.value
    }

    val underline = mutableStateOf(false)

    fun toggleUnderline() {
        underline.value = !underline.value
    }

    val lineThrough = mutableStateOf(false)

    fun toggleLineThrough() {
        lineThrough.value = !lineThrough.value
    }

    val link = mutableStateOf(false)

    fun toggleLink() {
        link.value = !link.value
    }

    val color = mutableStateOf(false)

    fun toggleColor() {
        color.value = !color.value
    }
}