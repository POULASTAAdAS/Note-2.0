package com.example.di

import com.example.data.repository.NoteDataBaseOperation
import com.example.data.repository.NoteDataBaseOperationImpl
import com.example.utils.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo
            .createClient()
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single<NoteDataBaseOperation> {
        NoteDataBaseOperationImpl(get())
    }
}