package com.example.note.data.repository

import com.example.note.data.database.NoteDao
import com.example.note.domain.model.Note
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllByPinnedAndUpdateDate() = noteDao.getAllByPinnedAndUpdateDate()
    fun getAllByPinnedAndEdited() = noteDao.getAllByPinnedAndEdited()

    fun getOneById(id: Int) = noteDao.getOneById(id)
    fun getByCreateDate(createDate: String) = noteDao.getByCreateDate(createDate)

    suspend fun addOne(note: Note) = noteDao.addOne(note)
    suspend fun updateOne(note: Note) = noteDao.updateOne(note)
    suspend fun deleteOne(note: Note) = noteDao.deleteOne(note)
    suspend fun deleteMultiple(noteIdList: ArrayList<Int>) = noteDao.deleteMultiple(noteIdList)

    suspend fun deleteAll() = noteDao.deleteAll()

    fun searchNotes(searchQuery: String): Flow<List<Note>> = noteDao.searchNotes(searchQuery)
}