package com.example.note.data.database.recentlyDeleted

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.domain.model.RecentlyDeleted


@Database(entities = [RecentlyDeleted::class], version = 2, exportSchema = false)
abstract class RecentlyDeletedDatabase : RoomDatabase() {
    abstract fun recentlyDeletedDao(): RecentlyDeletedDao
}