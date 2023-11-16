package com.example.note.presentation.screen.recentlyDeleted

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.connectivity.NetworkObserver
import com.example.note.connectivity.NetworkObserverImpl
import com.example.note.data.repository.InternalDatabaseImpl
import com.example.note.data.repository.NoteRepositoryImpl
import com.example.note.data.repository.RecentlyDeletedRepositoryImpl
import com.example.note.domain.model.ApiRequest
import com.example.note.domain.model.InternalNote
import com.example.note.domain.model.Note
import com.example.note.domain.model.RecentlyDeletedNotes
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import com.example.note.utils.convertRecentlyDeletedNoteToNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyDeletedViewModel @Inject constructor(
    private val server: NetworkRepository,
    private val connectivity: NetworkObserverImpl,
    private val dataStoreOperation: DataStoreOperation,
    private val dbNote: NoteRepositoryImpl,
    private val dbInternal: InternalDatabaseImpl,
    private val dbRecentlyDeleted: RecentlyDeletedRepositoryImpl
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private val autoSync = mutableStateOf(true)
    private val tokenOrCookie = mutableStateOf("")

    init {
        readAutoSyncState()
        readTokenOrCookie()
    }

    private fun readAutoSyncState() {
        viewModelScope.launch {
            dataStoreOperation.readAutoSyncState().collect {
                autoSync.value = it
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

    //------------------------------------------------------
    var recentlyDeletedNotes = MutableStateFlow<List<RecentlyDeletedNotes>>(emptyList())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dbRecentlyDeleted.getAllByDeleteDate().collect {
                recentlyDeletedNotes.value = it
            }
        }
    }

    val dropDownState = mutableStateOf(false)

    fun changeDropDownState() {
        dropDownState.value = !dropDownState.value
    }

    fun deleteAllClicked() {
        dropDownState.value = false
        deleteAll()
    }

    fun recoverOne(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = convertRecentlyDeletedNoteToNote(dbRecentlyDeleted.recoverOne(id).first())

            dbNote.addOne(note)
                .also { newId ->
                    handleAddApiCall(
                        note = Note(
                            _id = newId.toInt(),
                            heading = note.heading,
                            content = note.content,
                            createDate = note.createDate,
                            updateDate = note.updateDate,
                            updateTime = note.updateTime,
                            edited = note.edited,
                            pinned = note.pinned,
                            syncState = note.syncState
                        )
                    )
                    deleteOne(id)
                }
        }
    }

    private fun updateSyncState(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dbNote.updateSyncState(id, true)
        }
    }

    private fun handleAddApiCall(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (autoSync.value && checkInternetConnection()) // if internet true api call
                server.addOne(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        note = note
                    )
                ).also {
                    if (it.data?.status != null && it.data!!.status) // if apiResponse true update syncState
                        updateSyncState(id = note._id)
                    else // if apiResponse false or some unexpected error add to dbInternal
                        dbInternal.addOne(
                            internalNote = InternalNote(
                                id = note._id,
                                heading = note.heading,
                                content = note.content,
                                createDate = note.createDate!!,
                                updateDate = note.updateDate!!,
                                updateTime = note.updateTime!!,
                                edited = note.edited,
                                pinned = note.pinned,
                                syncState = false,
                                insert = true
                            )
                        )
                }
            else dbInternal.addOne( // no internet add to dbInternal
                internalNote = InternalNote(
                    id = note._id,
                    heading = note.heading,
                    content = note.content,
                    createDate = note.createDate!!,
                    updateDate = note.updateDate!!,
                    updateTime = note.updateTime!!,
                    edited = note.edited,
                    pinned = note.pinned,
                    syncState = false,
                    insert = true
                )
            )
        }
    }


    fun deleteOne(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRecentlyDeleted.deleteOne(id)
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRecentlyDeleted.deleteAll()
        }
    }
}