package com.example.note.data.repository

import com.example.note.data.database.recentlyDeleted.RecentlyDeletedDao
import com.example.note.domain.model.RecentlyDeleted
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class RecentlyDeletedRepositoryImpl @Inject constructor(
    private val recentlyDeletedDao: RecentlyDeletedDao
) {
    fun getAllByDeleteDate() = recentlyDeletedDao.getAllByDeleteDate()

    fun recoverOne(id: Int) = recentlyDeletedDao.recoverOne(id)
    fun recoverMultiple(listOfId: ArrayList<Int>) = recentlyDeletedDao.recoverMultiple(listOfId)

    suspend fun addOne(recentlyDeleted: RecentlyDeleted) = recentlyDeletedDao.addOne(recentlyDeleted)

    suspend fun deleteOne(recentlyDeleted: RecentlyDeleted) = recentlyDeletedDao.deleteOne(recentlyDeleted)
    suspend fun deleteMultiple(listOfId: ArrayList<Int>) = recentlyDeletedDao.deleteMultiple(listOfId)

    suspend fun deleteAll() = recentlyDeletedDao.deleteAll()
}