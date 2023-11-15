package com.example.note.data.database.recentlyDeleted

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.note.domain.model.RecentlyDeletedNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyDeletedDao {
    @Query("select * from recentlyDeletedTable order by deleteDate desc")
    fun getAllByDeleteDate(): Flow<List<RecentlyDeletedNotes>>

    @Query("select * from recentlyDeletedTable where id=:id")
    fun recoverOne(id: Int): Flow<RecentlyDeletedNotes>

    @Query("select * from recentlyDeletedTable where id in (:listOfId)")
    fun recoverMultiple(listOfId: ArrayList<Int>): Flow<List<RecentlyDeletedNotes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOne(recentlyDeletedNotes: RecentlyDeletedNotes)

    @Query("delete from recentlyDeletedTable where id=:id")
    suspend fun deleteOne(id: Int)

    @Query("delete from recentlyDeletedTable where id in (:listOfId)")
    suspend fun deleteMultiple(listOfId: ArrayList<Int>)

    @Query("delete from recentlyDeletedTable")
    suspend fun deleteAll()
}