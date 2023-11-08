package com.example.note.data.repository

import com.example.note.data.database.addUpdate.InternalDao
import com.example.note.domain.model.ApiNote
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class InternalDatabaseImpl @Inject constructor(
    private val dao: InternalDao
) {
    suspend fun addOne(apiNote: ApiNote) = dao.addOne(apiNote)
    suspend fun upsert(apiNote: ApiNote) = dao.upsert(apiNote)

    suspend fun deleteOne(id: Int) = dao.deleteOne(id)

    fun getAll() = dao.getAll()
    suspend fun deleteAll() = dao.deleteAll()
}