package com.example.note.data.database

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
    @Query("select * from noteTable order by pinned desc , updateDate desc")
    fun getAllByPinnedAndUpdateDate(): Flow<List<Note>>

    @Query("select * from noteTable order by pinned desc , edited desc")
    fun getAllByPinnedAndEdited(): Flow<List<Note>>

    @Query("select * from noteTable where id=:id")
    fun getOneById(id: Int): Flow<Note>

    @Query("select * from noteTable where createDate=:createDate")
    fun getByCreateDate(createDate: String): Flow<List<Note>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOne(note: Note)

    @Update
    suspend fun updateOne(note: Note)

    @Delete
    suspend fun deleteOne(note: Note)

    @Query("delete from noteTable where id in (:noteIdList)")
    suspend fun deleteMultiple(noteIdList: ArrayList<Int>)

    @Query("delete from noteTable")
    suspend fun deleteAll()


    @Query("select * from noteTable where heading like :searchQuery or content like :searchQuery order by updateDate desc")
    fun searchNotes(searchQuery: String): Flow<List<Note>>
}