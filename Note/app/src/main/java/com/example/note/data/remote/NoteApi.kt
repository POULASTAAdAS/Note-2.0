package com.example.note.data.remote

import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NoteApi {
    @POST("/login_signup")
    suspend fun loginSignup(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("/get_all")
    suspend fun getAll(
        @Header("Authorization")
        token: String
    ): ApiResponse
}