package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.data.database.NoteDao
import com.example.note.data.database.NoteDatabase
import com.example.note.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteDatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase = Room.databaseBuilder(
        context = context,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteDao(
        database: NoteDatabase
    ): NoteDao = database.noteDao()
}