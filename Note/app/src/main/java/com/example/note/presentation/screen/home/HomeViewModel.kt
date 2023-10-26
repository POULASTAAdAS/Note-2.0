package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation
) : ViewModel() {
    private var apiResponse: MutableState<DataOrException<ApiResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())

    private val loginState = mutableStateOf(false)
    private val token = mutableStateOf("")

    init {
        viewModelScope.launch {
            dataStoreOperation.readJWTToken().collect {
                token.value = "Bearer $it"
            }

            dataStoreOperation.readSignedInState().collect {
                loginState.value = it
            }
        }
    }

    fun getAllNoteIfAny() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!loginState.value) apiResponse.value = networkRepository.getAll(token = token.value)

            Log.d("apiResponseData" , apiResponse.value.data?.listOfNote.toString())
        }
    }
}