package com.example.note.data.repository

import com.example.note.data.database.note.NoteDao
import com.example.note.domain.model.Note
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllByPinnedAndCreateDate() = noteDao.getAllByPinnedAndCreateDate()
    fun getAllByPinnedAndEdited() = noteDao.getAllByPinnedAndEdited()

    fun getOneById(id: Int) = noteDao.getOneById(id)
    fun getMultipleById(listOfId: ArrayList<Int>) = noteDao.getMultipleById(listOfId)

    fun getByCreateDate(createDate: String) = noteDao.getByCreateDate(createDate)

    suspend fun addOne(note: Note) = noteDao.addOne(note)
    suspend fun updateOne(note: Note) = noteDao.updateOne(note)

    suspend fun deleteOne(note: Note) = noteDao.deleteOne(note)
    suspend fun deleteMultiple(noteIdList: ArrayList<Int>) = noteDao.deleteMultiple(noteIdList)

    suspend fun deleteAll() = noteDao.deleteAll()

    fun searchNotes(searchQuery: String): Flow<List<Note>> = noteDao.searchNotes(searchQuery)

    suspend fun updateSyncState(id: Int, syncState: Boolean) = noteDao.updateSyncState(id , syncState)
    suspend fun updatePinedState(id: Int) = noteDao.updatePinedState(id)
}