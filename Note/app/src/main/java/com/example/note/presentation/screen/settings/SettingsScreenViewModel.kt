package com.example.note.presentation.screen.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.connectivity.NetworkObserver
import com.example.note.connectivity.NetworkObserverImpl
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/*
todo add username to dataStore operation
 change apiResponse add username get username from email
 add time to note
 change selected screen show last edited date and time
*/

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val connectivity: NetworkObserverImpl,
    private val server: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
) : ViewModel() {
    var network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)
        private set
    var autoSync = mutableStateOf(true)
        private set
    var sortState = mutableStateOf(true)
        private set

    private val tokenOrCookie = mutableStateOf("")

    var userName = mutableStateOf("User")
        private set
    var oldUserName: String = ""
        private set

    init {
        readNetworkStatus()
        readAutoSyncState()
        readTokenOrCookie()
        readSortState()
        readUserName()
    }

    private fun readNetworkStatus() {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
            }
        }
    }

    private fun readTokenOrCookie() {
        viewModelScope.launch {
            dataStoreOperation.readJWTTokenOrSession().collect {
                tokenOrCookie.value = it
            }
        }
    }

    private fun readAutoSyncState() {
        viewModelScope.launch {
            dataStoreOperation.readAutoSyncState().collect {
                autoSync.value = it
            }
        }
    }

    private fun readSortState() {
        viewModelScope.launch {
            dataStoreOperation.readSortState().collect {
                sortState.value = it
            }
        }
    }

    private fun readUserName() {
        viewModelScope.launch {
            dataStoreOperation.readUserName().collect {
                userName.value = it
                oldUserName = it
            }
        }
    }

    private fun updateAutoSyncState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveAutoSyncState(autoSync.value)
        }
    }


    fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    var userNameCardState = mutableStateOf(false)
        private set

    fun changeUserNameText(value: String) {
        userName.value = value
    }

    fun changeUserNameCardState() {
        userNameCardState.value = !userNameCardState.value
    }

    fun saveUserName() {
        changeUserName()
        updateToServer()
    }

    private fun changeUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveUserName(userName.value.trim())
        }
    }

    private fun updateToServer() {
        // TODO server modification and app modification //// logInResponse apiRequest
    }

    fun userNameUpdateCancelClicked() {
        userName.value = oldUserName
    }

    fun toggleSyncSwitch(value: Boolean) {
        autoSync.value = value
        updateAutoSyncState()
    }

    fun updateSortState(value: Boolean?) {
        if (value != null)
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreOperation.saveSortState(value)
            }
    }
}


