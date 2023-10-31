package com.example.note.presentation.screen.neww

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewViewModel @Inject constructor() : ViewModel() {
    fun temp() {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 1..10) {
                delay(300)
                Log.d("tag", i.toString())
            }
        }
    }
}