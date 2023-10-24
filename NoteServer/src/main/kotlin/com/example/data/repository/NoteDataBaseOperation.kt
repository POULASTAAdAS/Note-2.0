package com.example.data.repository

import com.example.model.Note

interface NoteDataBaseOperation {
    suspend fun getAll(): List<Note>

    suspend fun addOne(note: Note): Boolean
    suspend fun addMultiple(listOfNote: List<Note>): Boolean

    suspend fun upDateOne(note: Note): Boolean
    suspend fun updateMultiple(listOfNote: List<Note>)

    suspend fun deleteOne(_id: String): Boolean
}