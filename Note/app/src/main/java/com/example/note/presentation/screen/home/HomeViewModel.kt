package com.example.note.presentation.screen.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.connectivity.NetworkObserver
import com.example.note.connectivity.NetworkObserverImpl
import com.example.note.data.remote.DataOrException
import com.example.note.data.repository.InternalDatabaseImpl
import com.example.note.data.repository.NoteRepositoryImpl
import com.example.note.data.repository.RecentlyDeletedRepositoryImpl
import com.example.note.domain.model.ApiRequest
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.InternalNote
import com.example.note.domain.model.Note
import com.example.note.domain.model.RecentlyDeletedNotes
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import com.example.note.navigation.Screens
import com.example.note.utils.Constants.BASE_URL
import com.example.note.utils.convertListOfInternalNoteToNote
import com.example.note.utils.getCurrentDate
import com.example.note.utils.getListOfIdFromInternalNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.CookieManager
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkObserverImpl,
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val cookieManager: CookieManager,
    private val dbNote: NoteRepositoryImpl,
    private val dbRecentlyDeleted: RecentlyDeletedRepositoryImpl,
    private val dbInternal: InternalDatabaseImpl
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination get() = _startDestination.asStateFlow()

    private val _allData = MutableStateFlow<List<Note>>(emptyList())
    val allData: StateFlow<List<Note>> = _allData

    private fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (sortState.value)
                dbNote.getAllByPinnedAndCreateDate().collect {
                    _allData.value = it
                    _isLoading.value = false
                }
            else dbNote.getAllByPinnedAndEdited().collect {
                _allData.value = it
                _isLoading.value = false
            }
        }
    }


    var network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)
        private set

    init {
        viewModelScope.launch {
            dataStoreOperation.readSignedInState().collect {
                _startDestination.value = if (it) Screens.Home.path else Screens.Login.path
                getAllData()
            }
        }
    }


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
    private val sortState = mutableStateOf(true)
    private val noteView = mutableStateOf(false)


    init {
        readAutoSyncState()
        readSortState()
        readNoteViewSate()
    }


    private fun readAutoSyncState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.readAutoSyncState().collect {
                autoSync.value = it
                delay(400)
                if (!it) autoSyncText.value = "On Auto Sync"
                else autoSyncText.value = "Off Auto Sync"
            }
        }
    }

    private fun readSortState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.readSortState().collect {
                sortState.value = it
                delay(400)
                if (it) sortStateText.value = "Sort by Last Edited"
                else sortStateText.value = "Sort by Create Date"
            }
        }
    }

    private fun readNoteViewSate() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreOperation.readNoteViewState().collect {
                noteView.value = it
                delay(400)
                noteViewText.value = if (it) "Grid view"
                else "List View"
            }
        }
    }

    private suspend fun updateAutoSync() =
        dataStoreOperation.saveAutoSyncState(autoSync.value)


    private suspend fun updateSortState() =
        dataStoreOperation.saveSortState(sortState.value)

    private suspend fun updateNoteView() =
        dataStoreOperation.saveNoteViewState(noteView.value)


    private fun performInitialApiCall() {
        performInsertApiCallOnStart()
        performUpdateApiCallOnStart()
        performDeleteApiCallOnStart()
    }


    private fun performInsertApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToInsert(true).collect { listOfInternalNote ->
                if (listOfInternalNote.isNotEmpty())
                    networkRepository.addMultiple(
                        token = "Bearer ${tokenOrCookie.value}",
                        request = ApiRequest(
                            listOfNote = convertListOfInternalNoteToNote(listOfInternalNote)
                        )
                    ).also { apiResponse ->
                        if (apiResponse.data != null && apiResponse.data!!.status) {
                            dbInternal.deleteMultiple(
                                listOfId = getListOfIdFromInternalNote(listOfInternalNote)
                            )

                            listOfInternalNote.forEach {
                                updateSyncState(
                                    id = it.id,
                                    state = true
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun performUpdateApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToUpdate(true).collect { listOfInternalNote ->
                if (listOfInternalNote.isNotEmpty())
                    networkRepository.updateMultiple(
                        token = "Bearer ${tokenOrCookie.value}",
                        request = ApiRequest(
                            listOfNote = convertListOfInternalNoteToNote(listOfInternalNote)
                        )
                    ).also { apiResponse ->
                        if (apiResponse.data != null && apiResponse.data!!.status) {
                            dbInternal.deleteMultiple(
                                listOfId = getListOfIdFromInternalNote(listOfInternalNote)
                            )

                            listOfInternalNote.forEach {
                                updateSyncState(
                                    id = it.id,
                                    state = true
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun performDeleteApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToDelete(true).collect { listOfId ->
                if (listOfId.isNotEmpty())
                    networkRepository.deleteMultiple(
                        token = "Bearer ${tokenOrCookie.value}",
                        request = ApiRequest(
                            listOfId = listOfId
                        )
                    ).also { apiResponse ->
                        if (apiResponse.data != null && apiResponse.data!!.status)
                            dbInternal.deleteMultiple(
                                listOfId = listOfId
                            )
                    }
            }
        }
    }


    // ----------------------------------------------------------------------------------
    private val appOpened = mutableStateOf(true)

    private val _apiResponse: MutableState<DataOrException<ApiResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())
//    val apiResponse: State<DataOrException<ApiResponse, Boolean, Exception>> get() = _apiResponse

    val showCircularProgressIndicator = mutableStateOf(false)

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
    }

    private suspend fun readTokenOrCookie() {
        dataStoreOperation.readJWTTokenOrSession().collect {
            tokenOrCookie.value = it
            readFirstTimeLoginState()
        }
    }

    private suspend fun readFirstTimeLoginState() {
        dataStoreOperation.readFirstTimeLoginState().collect {
            firstTimeLogIn = it
            readLogInType()
        }
    }

    private suspend fun readLogInType() {
        dataStoreOperation.readAuthType().collect {
            logInType = it
            setCookie()
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

            if (firstTimeLogIn!!) getAllApiData(tokenOrCookie.value)
            else performInitialApiCall()
            appOpened.value = false
        }
    }

    private fun getAllApiData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _apiResponse.value = networkRepository.getAll("Bearer $token")
            updateFirstTimeLoginSate()

            val temp = _apiResponse.value.data?.listOfNote
            if (!temp.isNullOrEmpty()) {
                showCircularProgressIndicator.value = true
                storeAllToDB(temp)
            }
        }
    }


    private fun handleAddApiCall(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (autoSync.value && checkInternetConnection()) // if internet true api call
                networkRepository.addOne(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        note = note
                    )
                ).also {
                    if (it.data?.status != null && it.data!!.status) // if apiResponse true update syncState
                        updateSyncState(
                            id = note._id,
                            state = true
                        ).also {
                            showCircularProgressIndicator.value = false
                        }
                    else // if apiResponse false or some unexpected error add to dbAddUpdate
                        dbInternal.addOne(
                            internalNote = InternalNote(
                                id = note._id,
                                heading = note.heading,
                                content = note.content,
                                createDate = note.createDate!!,
                                updateDate = note.updateDate!!,
                                edited = note.edited,
                                pinned = note.pinned,
                                syncState = false,
                                insert = true
                            )
                        ).also {
                            showCircularProgressIndicator.value = false
                        }
                }
            else dbInternal.addOne( // no internet add to dbAddUpdate
                internalNote = InternalNote(
                    id = note._id,
                    heading = note.heading,
                    content = note.content,
                    createDate = note.createDate!!,
                    updateDate = note.updateDate!!,
                    edited = note.edited,
                    pinned = note.pinned,
                    syncState = false,
                    insert = true
                )
            ).also {
                showCircularProgressIndicator.value = false
            }
        }
    }

    private fun handleUpdateApiCall(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (autoSync.value && checkInternetConnection())
                networkRepository.updateOne( // if internet true api call
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        note = note
                    )
                ).also {
                    if (it.data?.status != null && it.data!!.status)
                        updateSyncState( // if apiResponse true update syncState and delete from dbAddUpdate
                            id = note._id,
                            state = true
                        )
                            .also {
                                dbInternal.deleteOne(id = note._id)
                                showCircularProgressIndicator.value = false
                            }
                    else // if apiResponse false or some unexpected error add to dbAddUpdate
                        dbInternal.upsert( // if exists then update else insert
                            internalNote = InternalNote(
                                id = note._id,
                                heading = note.heading,
                                content = note.content,
                                createDate = note.createDate!!,
                                updateDate = note.updateDate!!,
                                edited = note.edited,
                                pinned = note.pinned,
                                syncState = false,
                                update = true
                            )
                        ).also {
                            showCircularProgressIndicator.value = false
                        }
                }
            else dbInternal.upsert( // no internet add to dbAddUpdate
                internalNote = InternalNote(
                    id = note._id,
                    heading = note.heading,
                    content = note.content,
                    createDate = note.createDate!!,
                    updateDate = note.updateDate!!,
                    edited = note.edited,
                    pinned = note.pinned,
                    syncState = false,
                    update = true
                )
            ).also {
                updateSyncState(id = note._id, state = false) // no internet update syncState
                showCircularProgressIndicator.value = false
            }
        }
    }

    private fun handleDeleteApiCall(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (autoSync.value && checkInternetConnection()) // if internet true api call
                networkRepository.deleteOne(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        listOfId = listOf(
                            note._id
                        )
                    )
                ).also {
                    if (it.data == null || !it.data!!.status) // if apiResponse false or some unexpected error add to dbInternal
                        if (note.heading != null && note.content != null) // edge case when both are null we are aborting internal db save
                            dbInternal.upsert( // if exists then update else insert
                                internalNote = InternalNote(
                                    id = note._id,
                                    heading = note.heading,
                                    content = note.content,
                                    createDate = note.createDate!!,
                                    updateDate = note.updateDate!!,
                                    edited = note.edited,
                                    pinned = note.pinned,
                                    syncState = false,
                                    delete = true
                                )
                            ).also {
                                showCircularProgressIndicator.value = false
                            }
                }
            else // if no internet
                if (note.heading != null && note.content != null) // edge case when both are null we are aborting internal db save
                    dbInternal.upsert( // if exists then update else insert
                        internalNote = InternalNote(
                            id = note._id,
                            heading = note.heading,
                            content = note.content,
                            createDate = note.createDate!!,
                            updateDate = note.updateDate!!,
                            edited = note.edited,
                            pinned = note.pinned,
                            syncState = false,
                            delete = true
                        )
                    ).also {
                        showCircularProgressIndicator.value = false
                    }
                else showCircularProgressIndicator.value = false
        }
    }


    private fun storeAllToDB(listOfNote: List<Note>) {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNote.forEach {
                try {
                    dbNote.addOne(it)
                } catch (e: IOException) {
                    Log.d("error adding one: ", e.message.toString())
                }
            }
            showCircularProgressIndicator.value = false
        }
    }

    // -------------------------------------------------------------------------------
    val showCustomToast = mutableStateOf(false)

    val searchText = mutableStateOf("")

    val heading = mutableStateOf("")
    val content = mutableStateOf("")

    val searchOpen = mutableStateOf(false)
    val searchTriggered = mutableStateOf(false)


    val selectAll = mutableStateOf(false)
    val noteEditState = mutableStateOf(false)

    val listOfId = MutableStateFlow(ArrayList<Int>())
    val listOfIdCount = mutableIntStateOf(0)

    var searchResult = MutableStateFlow<List<Note>>(emptyList())
        private set

    val expandState = mutableStateOf(false)

    val autoSyncText = mutableStateOf("Off Auto Sync") // On Auto Sync
    val sortStateText = mutableStateOf("Sort by Last Edited") // Sort by Create Date
    val noteViewText = mutableStateOf("List View") // Grid view

    fun changeExpandState() {
        expandState.value = !expandState.value
    }

    fun turnOnAutoSync() {
        autoSync.value = true
        viewModelScope.launch(Dispatchers.IO) {
            updateAutoSync()
            showCustomToast.value = false
        }
    }

    fun changeAutoSync(context: Context) {
        expandState.value = false
        autoSync.value = !autoSync.value
        viewModelScope.launch(Dispatchers.IO) {
            updateAutoSync()
        }

        if (!autoSync.value)
            Toast.makeText(
                context,
                "Turn on auto sync to save your notes to the server",
                Toast.LENGTH_LONG
            ).show()
    }

    fun changeSortState() {
        expandState.value = false
        sortState.value = !sortState.value
        viewModelScope.launch(Dispatchers.IO) {
            updateSortState()
        }
    }

    fun changeNoteView() {
        expandState.value = false
        noteView.value = !noteView.value
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteView()

        }
    }

    private fun listOfIdCountAdd() = listOfIdCount.intValue++
    private fun listOfIdCountMinus() = listOfIdCount.intValue--

    fun handleAddOrRemoveOfIdFromListOfId(id: Int, selectState: Boolean) {
        if (!listOfId.value.contains(id)) {
            if (selectState) {
                listOfId.value.add(id)
                listOfIdCountAdd()
            }
        } else {
            if (!selectState) {
                listOfId.value.remove(id)
                listOfIdCountMinus()
            }
        }
    }

    fun showCustomToast() {
        showCustomToast.value = !checkInternetConnection()
    }


    fun changeHeadingText(text: String) {
        heading.value = text
    }

    fun changeContentText(value: String) {
        content.value = value
    }

    fun clearTextFields() {
        heading.value = ""
        content.value = ""
    }

    fun selectAll() {
        selectAll.value = !selectAll.value
    }

    fun changeNoteEditState(value: Boolean) {
        noteEditState.value = value
        if (selectAll.value) selectAll.value = false
        if (listOfIdCount.intValue > 0) listOfIdCount.intValue = 0
        if (listOfId.value.isNotEmpty()) listOfId.value.clear()
    }

    fun addAndPushToServer(createDate: String) {
        addOne(createDate)
        showCircularProgressIndicator.value = true
    }

    fun changeSearchText(text: String) {
        searchText.value = text

        if (searchText.value.trim().isNotEmpty()) {
            searchTriggered.value = true
            searchDatabase(searchQuery = text.trim())
        } else searchTriggered.value = false
    }


    fun searchIconClicked() {
        searchOpen.value = !searchOpen.value
    }

    fun clearClicked() {
        if (noteEditState.value) {
            noteEditState.value = false
            listOfId.value.clear()
            selectAll.value = false
            listOfIdCount.intValue = 0
        } else
            if (searchText.value.isEmpty()) {
                searchIconClicked()
                searchTriggered.value = false
            } else searchText.value = ""
    }

    fun navigatedToDetailsScreenCleanUp() {
        if (searchTriggered.value) {
            searchResult.value = emptyList()
            searchText.value = ""
            searchTriggered.value = false
            searchOpen.value = false
        }
    }

    // local database operations
    //---------------------------------------------------------------------------------------
    private val noteID = mutableIntStateOf(0)
    val createDate = mutableStateOf("")
    private val edited = mutableIntStateOf(0)

    private val note = mutableStateOf(Note())

    private fun searchDatabase(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbNote.searchNotes(searchQuery = "%$searchQuery%").collect {
                searchResult.value = it
            }
        }
    }

    private fun updateSyncState(id: Int, state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dbNote.updateSyncState(id, state)
        }
    }


    fun updatePinnedState() {
        showCircularProgressIndicator.value = true
        noteEditState.value = false
        listOfIdCount.intValue = 0
        selectAll.value = false

        viewModelScope.launch {
            listOfId.value.forEach {
                updatePinnedState(it)
            }.also {
                listOfId.value.clear()
                showCircularProgressIndicator.value = false
            }
        }
    }


    private suspend fun updatePinnedState(id: Int) =
        dbNote.updatePinedState(id)


    fun setNoteID(id: Int) { // called from homeScreen(navigation.kt) when navigating to selected screen
        noteID.intValue = id
        getNoteById()
    }

    private fun getNoteById() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dbNote.getOneById(noteID.intValue)
                    .collect { // this will populate all the filed which are observed from selected screen
                        heading.value = it.heading!!
                        content.value = it.content!!
                        createDate.value = it.createDate!!
                        edited.intValue = it.edited
                        note.value = it
                    }
            } catch (e: Exception) { // deleteOne called form selectedScreen triggering error on getOneById.collect method even if not called
                Log.d("error reason god knows", e.message.toString())
            }
        }
    }

    private fun addOne(createDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbNote.addOne(
                note = Note(
                    heading = heading.value,
                    content = content.value,
                    createDate = createDate,
                    updateDate = createDate
                )
            ).also {
                handleAddApiCall(
                    note = Note(
                        _id = it.toInt(),
                        heading = heading.value,
                        content = content.value,
                        createDate = createDate,
                        updateDate = createDate,
                        edited = 0,
                        pinned = false,
                        syncState = true
                    )
                )
            }
        }
    }


    fun updateSingle() {
        if (
            heading.value.trim().isEmpty() &&
            content.value.trim().isEmpty()
        ) deleteOne(note = Note(noteID.intValue), true)
        else
            if (
                heading.value.trim() != note.value.heading ||
                content.value.trim() != note.value.content
            ) updateOne()
    }


    private fun updateOne() {
        showCircularProgressIndicator.value = true

        viewModelScope.launch(Dispatchers.IO) {
            dbNote.updateOne(
                note = Note(
                    _id = noteID.intValue,
                    heading = heading.value,
                    content = content.value,
                    createDate = note.value.createDate,
                    updateDate = note.value.updateDate,
                    edited = ++edited.intValue,
                    pinned = note.value.pinned,
                    syncState = note.value.syncState
                )
            ).also {
                handleUpdateApiCall(
                    note = Note(
                        _id = noteID.intValue,
                        heading = heading.value,
                        content = content.value,
                        createDate = note.value.createDate,
                        updateDate = note.value.updateDate,
                        edited = ++edited.intValue,
                        pinned = note.value.pinned,
                        syncState = true
                    )
                )
            }
        }
    }


    fun deleteCalledFromHomeScreen() {
        showCircularProgressIndicator.value = true
        noteEditState.value = false
        selectAll.value = false
        listOfIdCount.intValue = 0

        viewModelScope.launch(Dispatchers.IO) {
            dbNote.getMultipleById(
                listOfId = listOfId.value
            ).collect {
                for (note in it) {
                    putOneToRecentlyDeletedDatabase(note)
                    handleDeleteApiCall(note)
                }

                dbNote.deleteMultiple(noteIdList = listOfId.value)
                    .also {
                        showCircularProgressIndicator.value = false
                    }
                listOfId.value.clear()
            }
        }
    }

    fun deleteCalledFromSelectedScreen() = deleteOne(note = note.value, false)

    private fun deleteOne(note: Note, empty: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dbNote.deleteOne(note)
                .also {
                    if (empty) handleDeleteApiCall(note = Note(_id = note._id))
                    else {
                        putOneToRecentlyDeletedDatabase(note)
                        handleDeleteApiCall(note = note)
                    }
                }
        }
    }

    private fun putOneToRecentlyDeletedDatabase(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRecentlyDeleted.addOne(
                recentlyDeletedNotes = RecentlyDeletedNotes(
                    id = note._id,
                    heading = note.heading,
                    content = note.content,
                    createDate = note.createDate!!,
                    updateDate = note.updateDate!!,
                    edited = note.edited,
                    pinned = note.pinned,
                    syncState = note.syncState,
                    deleteDate = getCurrentDate()
                )
            )
        }
    }
}