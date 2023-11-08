package com.example.note.data.database.addUpdate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.note.domain.model.ApiNote
import kotlinx.coroutines.flow.Flow

@Dao
interface InternalDao {
    @Insert
    suspend fun addOne(apiNote: ApiNote)

    @Upsert
    suspend fun upsert(apiNote: ApiNote)

    @Query("delete from internalTable where id=:id")
    suspend fun deleteOne(id: Int)

    @Query("select * from internalTable")
    fun getAll(): Flow<List<ApiNote>>

    @Query("delete from internalTable")
    suspend fun deleteAll()
}