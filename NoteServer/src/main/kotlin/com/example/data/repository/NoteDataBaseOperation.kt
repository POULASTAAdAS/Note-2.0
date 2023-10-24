package com.example.data.repository

import com.example.model.Note
import com.example.model.User
import com.example.utils.UserExists


interface NoteDataBaseOperation {
    suspend fun createGoogleAuthenticatedUser(user: User): UserExists
    suspend fun createJWTAuthenticatedUser(user: User): UserExists

    suspend fun getAllNoteForJWTAuthenticatedUser(email: String): List<Note>
    suspend fun getAllNoteForGoogleAuthenticatedUser(sub: String): List<Note>

    suspend fun addOneForJWTUser(note: Note, email: String): Boolean
    suspend fun addOneForGoogleUser(note: Note, sub: String): Boolean
}