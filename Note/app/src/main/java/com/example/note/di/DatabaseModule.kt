package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.data.database.addUpdate.InternalDao
import com.example.note.data.database.addUpdate.InternalDatabase
import com.example.note.data.database.note.NoteDao
import com.example.note.data.database.note.NoteDatabase
import com.example.note.data.database.recentlyDeleted.RecentlyDeletedDao
import com.example.note.data.database.recentlyDeleted.RecentlyDeletedDatabase
import com.example.note.utils.Constants.INTERNAL_DATABASE_NAME
import com.example.note.utils.Constants.NOTE_DATABASE_NAME
import com.example.note.utils.Constants.RECENTLY_DELETED_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase = Room.databaseBuilder(
        context = context,
        NoteDatabase::class.java,
        NOTE_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteDao(
        database: NoteDatabase
    ): NoteDao = database.noteDao()


    @Provides
    @Singleton
    fun provideRecentlyDeletedDatabase(
        @ApplicationContext context: Context
    ): RecentlyDeletedDatabase = Room.databaseBuilder(
        context,
        RecentlyDeletedDatabase::class.java,
        RECENTLY_DELETED_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideRecentlyDeletedDao(
        database: RecentlyDeletedDatabase
    ): RecentlyDeletedDao = database.recentlyDeletedDao()


    @Provides
    @Singleton
    fun provideInternalDB(
        @ApplicationContext context: Context
    ): InternalDatabase = Room.databaseBuilder(
        context,
        InternalDatabase::class.java,
        INTERNAL_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideInternalDao(
        database: InternalDatabase
    ): InternalDao = database.internalDao()
}