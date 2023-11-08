package com.example.note.data.database.recentlyDeleted

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.note.domain.model.RecentlyDeleted
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyDeletedDao {
    @Query("select * from recentlyDeletedTable order by deleteDate desc")
    fun getAllByDeleteDate(): Flow<List<RecentlyDeleted>>

    @Query("select * from recentlyDeletedTable where id=:id")
    fun recoverOne(id: Int): Flow<RecentlyDeleted>

    @Query("select * from recentlyDeletedTable where id in (:listOfId)")
    fun recoverMultiple(listOfId: ArrayList<Int>): Flow<List<RecentlyDeleted>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOne(recentlyDeleted: RecentlyDeleted)

    @Delete
    suspend fun deleteOne(recentlyDeleted: RecentlyDeleted)

    @Query("delete from recentlyDeletedTable where id in (:listOfId)")
    suspend fun deleteMultiple(listOfId: ArrayList<Int>)

    @Query("delete from recentlyDeletedTable")
    suspend fun deleteAll()
}