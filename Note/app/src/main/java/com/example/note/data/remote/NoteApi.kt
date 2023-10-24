package com.example.note.data.remote

import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApi {
    @POST("login_signup")
    suspend fun loginSignup(
        @Body request: LoginRequest
    ): LoginResponse
}