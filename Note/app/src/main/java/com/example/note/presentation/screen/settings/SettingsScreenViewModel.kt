package com.example.note.presentation.screen.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.connectivity.NetworkObserver
import com.example.note.connectivity.NetworkObserverImpl
import com.example.note.data.repository.InternalDatabaseImpl
import com.example.note.data.repository.NoteRepositoryImpl
import com.example.note.data.repository.RecentlyDeletedRepositoryImpl
import com.example.note.domain.model.ApiRequest
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val connectivity: NetworkObserverImpl,
    private val server: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val dbNote: NoteRepositoryImpl,
    private val dbInternal: InternalDatabaseImpl,
    private val dbRecentlyDeleted: RecentlyDeletedRepositoryImpl
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

    var logOutCardState = mutableStateOf(false)
        private set

    var deleteAccountCardState = mutableStateOf(false)
        private set

    fun changeUserNameText(value: String) {
        userName.value = value
    }

    fun changeUserNameCardState() {
        logOutCardState.value = false
        deleteAccountCardState.value = false
        userNameCardState.value = !userNameCardState.value
    }

    fun changeLogoutCardState() {
        userNameCardState.value = false
        deleteAccountCardState.value = false
        logOutCardState.value = !logOutCardState.value
    }

    fun changeDeleteAccountCardState() {
        logOutCardState.value = false
        userNameCardState.value = false
        deleteAccountCardState.value = !deleteAccountCardState.value
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
        viewModelScope.launch(Dispatchers.IO) {
            server.updateUserName(
                token = "Bearer ${tokenOrCookie.value}",
                request = ApiRequest(
                    userName = userName.value.trim()
                )
            )
        }
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

    val isLoggedOut = mutableStateOf(false)
    val isAccountDeleted = mutableStateOf(false)

    val navigateToLogInScreen = mutableStateOf(false)

    fun logOutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoggedOut.value = true
            changeLogoutCardState()

            dbNote.deleteAll()
                .also {
                    reset()
                }
            dbInternal.deleteAll()
            dbRecentlyDeleted.deleteAll()
        }
    }

    fun deleteUser(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            isAccountDeleted.value = true
            changeDeleteAccountCardState()
            server.deleteUser(
                token = "Bearer ${tokenOrCookie.value}"
            ).also {
                if (it.data != null && it.data!!.status) {
                    reset()
                } else {
                    isAccountDeleted.value = false
                    Toast.makeText(
                        context,
                        "unable to delete account please try after some time",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun reset() {
        resetCookieOrToken()
        resetSortType()
        resetUserName()
        resetFirstTimeLoginState()
        resetSignedInState()
    }

    private fun resetCookieOrToken() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveUpdateJWTTokenOrSession("")
        }
    }

    private fun resetUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveUserName("User")
        }
    }

    private fun resetSortType() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveSortState(true)
        }
    }


    private fun resetSignedInState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveUpdateSignedInState(false)
            navigateToLogInScreen.value = true
        }
    }

    private fun resetFirstTimeLoginState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.saveFirstTimeLoginState(false)
        }
    }
}


