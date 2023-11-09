package com.example.note.data.repository

import com.example.note.data.database.addUpdate.InternalDao
import com.example.note.domain.model.InternalNote
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class InternalDatabaseImpl @Inject constructor(
    private val dao: InternalDao
) {
    suspend fun addOne(internalNote: InternalNote) = dao.addOne(internalNote)
    suspend fun upsert(internalNote: InternalNote) = dao.upsert(internalNote)

    suspend fun deleteOne(id: Int) = dao.deleteOne(id)

    fun getAllToInsert(insert: Boolean) = dao.getAllToInsert(insert)
    fun getAllToUpdate(update: Boolean) = dao.getAllToUpdate(update)
    fun getAllToDelete(delete: Boolean) = dao.getAllToDelete(delete)

    suspend fun deleteMultiple(listOfId: List<Int>) = dao.deleteMultiple(listOfId)
}