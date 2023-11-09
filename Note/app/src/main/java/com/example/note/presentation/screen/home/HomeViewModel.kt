package com.example.note.presentation.screen.home

import android.util.Log
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
import com.example.note.domain.model.RecentlyDeleted
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
            dbNote.getAllByPinnedAndUpdateDate().collect {
                _allData.value = it
            }
        }
    }

    private val autoSync = mutableStateOf(true)

    var network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)
        private set

    init {
        viewModelScope.launch {
            dataStoreOperation.readSignedInState().collect {
                _startDestination.value = if (it) Screens.Home.path else Screens.Login.path
                getAllData()
                _isLoading.value = false
                Log.d("readSignedInState", it.toString())
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


    init {
        Log.d("called" , "performInsertApiCallOnStart out")
        if (autoSync.value && checkInternetConnection()) {
            Log.d("called" , "performInsertApiCallOnStart")
            performInsertApiCallOnStart()
            performUpdateApiCallOnStart()
            performDeleteApiCallOnStart()
        }
    }


    private fun performInsertApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToInsert(true).collect { listOfInternalNote ->
                networkRepository.addMultiple(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        listOfNote = convertListOfInternalNoteToNote(listOfInternalNote)
                    )
                ).also { apiResponse ->
                    if (apiResponse.data != null && apiResponse.data!!.status)
                        dbInternal.deleteMultiple(
                            listOfId = getListOfIdFromInternalNote(listOfInternalNote)
                        )
                }
            }
        }
    }

    private fun performUpdateApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToUpdate(true).collect { listOfInternalNote ->
                networkRepository.updateMultiple(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        listOfNote = convertListOfInternalNoteToNote(listOfInternalNote)
                    )
                ).also { apiResponse ->
                    if (apiResponse.data != null && apiResponse.data!!.status)
                        dbInternal.deleteMultiple(
                            listOfId = getListOfIdFromInternalNote(listOfInternalNote)
                        )
                }
            }
        }
    }

    private fun performDeleteApiCallOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAllToDelete(true).collect { listOfId ->
                networkRepository.deleteMultiple(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        listOfId = listOf(listOfId.toString())
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

    private fun setCookie() {
        if (logInType != null) {
            if (logInType!!) {
                cookieManager.put(
                    URI.create(BASE_URL),
                    mapOf("Set-Cookie" to mutableListOf(tokenOrCookie.value))
                )
            }
            Log.d("call 5", "cookie set")


            if (firstTimeLogIn!!) getAllApiData(tokenOrCookie.value)
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


    private fun handleApiAddApiCall(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (autoSync.value && checkInternetConnection()) // if internet true api call
                networkRepository.addOne(
                    token = "Bearer ${tokenOrCookie.value}",
                    request = ApiRequest(
                        note = note
                    )
                ).also {
                    if (it.data?.status != null && it.data!!.status) // if apiResponse true update syncState
                        dbNote.updateSyncState(
                            id = note._id,
                            syncState = true
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
                        dbNote.updateSyncState( // if apiResponse true update syncState and delete from dbAddUpdate
                            id = note._id,
                            syncState = true
                        ).also {
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
                dbNote.updateSyncState( // no internet update syncState
                    id = note._id,
                    syncState = false
                )
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
                            note._id.toString()
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
                    Log.d("storeAllToDB exception", e.message.toString())
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
        viewModelScope.launch {
            if (!checkInternetConnection()) {
                showCustomToast.value = true
                delay(4000)
            } else showCustomToast.value = false
        }
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


    fun setNoteID(id: Int) { // called from homeScreen(navigation.kt) when navigating to selected screen
        noteID.intValue = id
        getNoteById()
    }

    private fun getNoteById() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dbNote.getOneById(noteID.intValue)
                    .collect { // this will populate all the filed which are observed from selected screen
                        Log.d("called", it.toString())
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
                handleApiAddApiCall(
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

        // todo make api call

        viewModelScope.launch(Dispatchers.IO) {
            dbNote.getMultipleById(
                listOfId = listOfId.value
            ).collect {
                for (note in it) putOneToRecentlyDeletedDatabase(note)

                dbNote.deleteMultiple(noteIdList = listOfId.value)
                    .also {
                        delay(800)
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
                recentlyDeleted = RecentlyDeleted(
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

    fun temp() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInternal.getAll().collect {
                for (i in it)
                    Log.d("addUpdateNote", i.toString())
            }
        }
    }
}