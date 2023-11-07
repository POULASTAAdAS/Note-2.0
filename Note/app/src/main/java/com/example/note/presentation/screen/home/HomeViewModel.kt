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
import com.example.note.data.repository.NoteRepositoryImpl
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.Note
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import com.example.note.navigation.Screens
import com.example.note.utils.Constants.BASE_URL
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
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val cookieManager: CookieManager,
    private val db: NoteRepositoryImpl,
    private val connectivity: NetworkObserverImpl
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination get() = _startDestination.asStateFlow()


    private val _allData = MutableStateFlow<List<Note>>(emptyList())
    val allData: StateFlow<List<Note>> = _allData

    private fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getAllByPinnedAndUpdateDate().collect {
                _allData.value = it
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
                _isLoading.value = false
                Log.d("readSignedInState", it.toString())
            }
        }
    }

    init { // not getting called if in the same init block
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
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
            appOpened.value = false // todo this may cause bug when making api call
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

    private fun storeAllToDB(listOfNote: List<Note>) {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNote.forEach {
                try {
                    db.addOne(it)
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
            db.searchNotes(searchQuery = "%$searchQuery%").collect {
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
                db.getOneById(noteID.intValue)
                    .collect { // this will populate all the filed which are observed from selected screen
                        heading.value = it.heading!!
                        content.value = it.content!!
                        createDate.value = it.createDate!!
                        edited.intValue = it.edited
                        note.value = it
                    }
            } catch (e: Exception) { // deleteOne called form selectedScreen triggering error on getOneById.collect note the method is not called
                Log.d("error reason god knows", e.message.toString())
            }
        }
    }

    private fun addOne(createDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            db.addOne(
                note = Note(
                    heading = heading.value,
                    content = content.value,
                    createDate = createDate,
                    updateDate = createDate
                )
            ).also {
                showCircularProgressIndicator.value = false // todo perform api call
            }
        }


    fun updateSingle() = if (
        heading.value.trim().isEmpty() &&
        content.value.trim().isEmpty()
    ) deleteOne(note = Note(noteID.intValue))
    else updateOne()

    private fun updateOne() { //todo add apiReq and first add network state
        viewModelScope.launch(Dispatchers.IO) {
            db.updateOne(
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
            )
        }
    }


    fun deleteCalledFromHomeScreen() {
        showCircularProgressIndicator.value = true
        noteEditState.value = false
        selectAll.value = false
        listOfIdCount.intValue = 0

        viewModelScope.launch(Dispatchers.IO) {
            db.deleteMultiple(noteIdList = listOfId.value)
                .run {
                    showCircularProgressIndicator.value = false
                    listOfId.value.clear()
                }
        }
    }

    fun deleteCalledFromSelectedScreen() {
        deleteOne(note = Note(_id = noteID.intValue))
    }

    private fun deleteOne(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteOne(note)
                .also {
                    showCircularProgressIndicator.value = false // todo api call
                }
        }
    }
}