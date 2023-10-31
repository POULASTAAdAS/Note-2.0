package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import com.example.note.utils.Constants.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.CookieManager
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val cookieManager: CookieManager
) : ViewModel() {

    private val _apiResponse: MutableState<DataOrException<ApiResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())
//    val apiResponse: State<DataOrException<ApiResponse, Boolean, Exception>> = _apiResponse

    val isData = mutableStateOf(false)

    private val tokenOrCookie = mutableStateOf("")

    private var firstTimeLogIn: Boolean? = null
    private var logInType: Boolean? = null

    val allSet = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500) // give time to write before reading data if logging in for first time
            readTokenOrCookie()
        }
    }

    private suspend fun updateFirstTimeLoginSate() {
        dataStoreOperation.saveFirstTimeLoginState(false)
        Log.d("call 3", "saveFirstTimeLoginState")
    }

    private suspend fun readTokenOrCookie() {
        dataStoreOperation.readJWTTokenOrSession().collect {
            tokenOrCookie.value = it
            Log.d("call 1", it)
            readFirstTimeLoginState()
        }
    }

    private suspend fun readFirstTimeLoginState() {
        dataStoreOperation.readFirstTimeLoginState().collect {
            Log.d("call 2", it.toString())
            firstTimeLogIn = it
            readLogInType()
        }
    }

    private suspend fun readLogInType() {
        dataStoreOperation.readAuthType().collect {
            logInType = it
            Log.d("call 4", it.toString())
            setCookie()
        }
    }


    fun getAll() {
        if (firstTimeLogIn != null) {
            if (firstTimeLogIn!!) {
                getAllData(tokenOrCookie.value)
            }
        }
    }

    private fun setCookie() {
        if (logInType != null) {
            if (logInType!!) {
                cookieManager.put(
                    URI.create(BASE_URL),
                    mapOf("Set-Cookie" to mutableListOf(tokenOrCookie.value))
                )
            }
            Log.d("call 5", "cookie set")
        }
    }


    private fun getAllData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _apiResponse.value = networkRepository.getAll("Bearer $token")
            Log.d("homeViewModel apiResponse", _apiResponse.value.data?.listOfNote.toString())

            updateFirstTimeLoginSate()
            if (firstTimeLogIn!!) allSet.value = true

            val temp = _apiResponse.value.data?.listOfNote

            if (!temp.isNullOrEmpty()) {
                isData.value = true
            }
        }
    }

    //-------------------------------------------------------------------------------

    fun temp() {
        viewModelScope.launch(Dispatchers.IO) {
            val temp = networkRepository.getAll("Bearer ${tokenOrCookie.value}")
            Log.d("data", temp.data?.listOfNote.toString())
        }
    }


    val searchText = mutableStateOf("")

    val noteSelected = mutableStateOf(false) // to delete
    val searchEnabled = mutableStateOf(false)

    fun changeSearchText(text: String) {
        searchText.value = text
    }

    fun searchIconClicked() {
        searchEnabled.value = !searchEnabled.value
    }

    fun clearClicked() {
        if (noteSelected.value)
            TODO()
        else
            if (searchText.value.isEmpty()) searchIconClicked()
            else searchText.value = ""
    }

    fun searchClicked() {
        // TODO local database search
    }
}