package com.example.note.data.database.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("select * from noteTable order by pinned desc , createDate desc")
    fun getAllByPinnedAndCreateDate(): Flow<List<Note>>

    @Query("select * from noteTable order by pinned desc , edited desc")
    fun getAllByPinnedAndEdited(): Flow<List<Note>>

    @Query("select * from noteTable where id=:id")
    fun getOneById(id: Int): Flow<Note>

    @Query("select * from noteTable where id in (:lisOffId)")
    fun getMultipleById(lisOffId: ArrayList<Int>): Flow<List<Note>>

    @Query("select * from noteTable where createDate=:createDate")
    fun getByCreateDate(createDate: String): Flow<List<Note>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOne(note: Note): Long

    @Update
    suspend fun updateOne(note: Note)


    @Query("update noteTable set syncState=:syncState where id=:id")
    suspend fun updateSyncState(id: Int, syncState: Boolean)

    // @Query("UPDATE noteTable SET pinned = 1 - pinned WHERE _id = :noteId")
    @Query("update noteTable set pinned = case when pinned = 1 then 0 else 1 end where id=:id")
    suspend fun updatePinedState(id: Int)

    @Delete
    suspend fun deleteOne(note: Note)

    @Query("delete from noteTable where id in (:noteIdList)")
    suspend fun deleteMultiple(noteIdList: ArrayList<Int>)

    @Query("delete from noteTable")
    suspend fun deleteAll()


    @Query("select * from noteTable where heading like :searchQuery or content like :searchQuery order by updateDate desc")
    fun searchNotes(searchQuery: String): Flow<List<Note>>
}