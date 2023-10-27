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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.CookieManager
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val cookieManager: CookieManager
) : ViewModel() {

    private var apiResponse: MutableState<DataOrException<ApiResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())

    init {
//        cookieManager.put("".toUri(), mapOf("Set-Cookie" to mutableListOf("2d4a8860474a07762ff477eb6bd31552")))
        viewModelScope.launch {




            delay(500)
            dataStoreOperation.readFirstTimeLoginState().collect {
//                if (it) getAll()
                getAll()
            }
        }
    }

    fun temp() {
        viewModelScope.launch {
            getAll()
        }
    }

    private suspend fun getAll() {
        dataStoreOperation.readJWTToken().collect {
            Log.d("cookies", cookieManager.cookieStore.cookies.toString()) // TODO cookie
            Log.d("cookies", cookieManager.cookieStore.urIs.toString())
            apiResponse.value = networkRepository.getAll(token = "USER_SESSION=a1055a90d468b3924459d8f99eee9814")
            dataStoreOperation.saveFirstTimeLoginState(false)
            Log.d("apiResponse", apiResponse.value.data?.listOfNote.toString())
        }
    }
}