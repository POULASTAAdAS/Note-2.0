package com.example.note.data.database.addUpdate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.domain.model.ApiNote

@Database(entities = [ApiNote::class], version = 1, exportSchema = false)
abstract class InternalDatabase : RoomDatabase() {
    abstract fun internalDao(): InternalDao
}