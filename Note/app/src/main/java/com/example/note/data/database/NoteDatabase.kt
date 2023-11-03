package com.example.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.domain.model.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}