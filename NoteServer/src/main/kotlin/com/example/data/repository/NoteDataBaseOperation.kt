package com.example.data.repository

import com.example.model.Note
import com.example.model.User
import com.example.utils.UserExists


interface NoteDataBaseOperation {
    suspend fun createGoogleAuthenticatedUser(user: User): UserExists
    suspend fun createJWTAuthenticatedUser(user: User): UserExists

    suspend fun getUserNameForJwtUser(email: String): String
    suspend fun getUserNameForGoogleUser(sub: String): String

    suspend fun getAllNoteForJWTAuthenticatedUser(email: String): List<Note>
    suspend fun getAllNoteForGoogleAuthenticatedUser(sub: String): List<Note>

    suspend fun addOneForJWTUser(note: Note, email: String): Boolean
    suspend fun addOneForGoogleUser(note: Note, sub: String): Boolean

    suspend fun addMultipleForJWTUser(listOfNote: List<Note>, email: String)
    suspend fun addMultipleForGoogleUser(listOfNote: List<Note>, sub: String)

    suspend fun updateOneForJWTUser(note: Note, email: String)
    suspend fun updateOneForGoogleUser(note: Note, sub: String)

    suspend fun updateMultipleForJWTUser(listOfNote: List<Note>, email: String)
    suspend fun updateMultipleForGoogleUser(listOfNote: List<Note>, sub: String)

    suspend fun updateUserNameOfJwtUser(name: String, email: String)
    suspend fun updateUserNameOfGoogleUser(name: String, sub: String)

    suspend fun deleteOneForJWTUser(_id: String, email: String)
    suspend fun deleteOneForGoogleUser(_id: String, sub: String)

    suspend fun deleteMultipleForJWTUser(listOf_id: List<String>, email: String)
    suspend fun deleteMultipleForGoogleUser(listOf_id: List<String>, sub: String)

    suspend fun deleteJWTUser(email: String)
    suspend fun deleteGoogleUser(sub: String)
}