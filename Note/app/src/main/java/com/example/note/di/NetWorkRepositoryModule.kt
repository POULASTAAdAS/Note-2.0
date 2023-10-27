package com.example.note.di

import com.example.note.data.remote.NoteApi
import com.example.note.data.repository.NetworkRepositoryImpl
import com.example.note.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkRepositoryModule {
    @Provides
    @Singleton
    fun provideNetworkRepository(
        noteApi: NoteApi
    ): NetworkRepository = NetworkRepositoryImpl(noteApi = noteApi)
}