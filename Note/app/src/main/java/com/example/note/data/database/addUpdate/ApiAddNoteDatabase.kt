package com.example.note.data.database.addUpdate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.domain.model.InternalNote

@Database(entities = [InternalNote::class], version = 1, exportSchema = false)
abstract class InternalDatabase : RoomDatabase() {
    abstract fun internalDao(): InternalDao
}