package com.example.note.data.remote

import com.example.note.domain.model.ApiRequest
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface NoteApi {
    @POST("/login_signup")
    suspend fun loginSignup(
        @Body request: LoginRequest
    ): LoginResponse

    //---------------------------------

    @GET("/get_all")
    suspend fun getAll(
        @Header("Authorization")
        token: String
    ): ApiResponse

    //---------------------------------

    @POST("/add_one")
    suspend fun addOne(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse

    @POST("/add_multiple")
    suspend fun addMultiple(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse

    //---------------------------------

    @PATCH("/update_one")
    suspend fun updateOne(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse

    @PATCH("/update_multiple")
    suspend fun updateMultiple(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse

    //---------------------------------

    @HTTP(method = "DELETE", path = "/delete_one", hasBody = true)
    suspend fun deleteOne(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse

    @HTTP(method = "DELETE", path = "/delete_multiple", hasBody = true)
    suspend fun deleteMultiple(
        @Header("Authorization")
        token: String,
        @Body request: ApiRequest
    ): ApiResponse
}