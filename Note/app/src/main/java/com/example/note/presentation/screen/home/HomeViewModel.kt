package com.example.note.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import com.example.note.navigation.Screens
import com.example.note.utils.Constants.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination get() = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreOperation.readSignedInState().collect {
                _startDestination.value = if (it) Screens.Home.path else Screens.Login.path
                _isLoading.value = false
                Log.d("readSignedInState", it.toString())
            }
        }
    }

    // ----------------------------------------------------------------------------------
    private val appOpened = mutableStateOf(true)

    private val _apiResponse: MutableState<DataOrException<ApiResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())
    val apiResponse: State<DataOrException<ApiResponse, Boolean, Exception>> get() = _apiResponse

    val isData = mutableStateOf(false)

    private val tokenOrCookie = mutableStateOf("")

    private var firstTimeLogIn: Boolean? = null
    private var logInType: Boolean? = null

    fun initialSet() {
        if (appOpened.value) {
            viewModelScope.launch(Dispatchers.IO) {
                readTokenOrCookie()
            }
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


    private fun getAll() {
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
            getAll()
        }
    }

    private fun getAllData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _apiResponse.value = networkRepository.getAll("Bearer $token")
            Log.d("homeViewModel apiResponse", _apiResponse.value.data?.listOfNote.toString())

            updateFirstTimeLoginSate()

            val temp = _apiResponse.value.data?.listOfNote
            if (!temp.isNullOrEmpty()) isData.value = true
        }
    }

    //-------------------------------------------------------------------------------
    val searchText = mutableStateOf("")

    val noteSelected = mutableStateOf(false) // to delete
    val searchEnabled = mutableStateOf(false)


    val heading = mutableStateOf("")
    val content = mutableStateOf("") // TODO


    fun changeHeadingText(text: String) {
        heading.value = text
    }

    fun clearHeading() {
        heading.value = ""
    }

    fun changeContentText(text: String) { // TODO
        content.value = text
    }


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