package com.example.note.data.database.recentlyDeleted

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.domain.model.RecentlyDeletedNotes


@Database(entities = [RecentlyDeletedNotes::class], version = 1, exportSchema = false)
abstract class RecentlyDeletedDatabase : RoomDatabase() {
    abstract fun recentlyDeletedDao(): RecentlyDeletedDao
}