package com.example.note.data.database.addUpdate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.note.domain.model.InternalNote
import kotlinx.coroutines.flow.Flow

@Dao
interface InternalDao {
    @Insert
    suspend fun addOne(internalNote: InternalNote)

    @Upsert
    suspend fun upsert(internalNote: InternalNote)

    @Query("delete from internalTable where id=:id")
    suspend fun deleteOne(id: Int)

    @Query("select * from internalTable where `insert`=:insert")
    fun getAllToInsert(insert: Boolean): Flow<List<InternalNote>>

    @Query("select * from internalTable where `update`=:update")
    fun getAllToUpdate(update: Boolean): Flow<List<InternalNote>>

    @Query("select id from internalTable where `delete`=:delete")
    fun getAllToDelete(delete: Boolean): Flow<List<Int>>

    @Query("delete from internalTable where id in (:listOfId)")
    suspend fun deleteMultiple(listOfId: List<Int>)

    @Query("delete from internalTable")
    suspend fun deleteAll()
}