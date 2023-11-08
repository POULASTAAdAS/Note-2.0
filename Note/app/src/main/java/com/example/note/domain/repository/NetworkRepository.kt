package com.example.note.domain.repository

import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.ApiRequest
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse

interface NetworkRepository {
    suspend fun loginSignUp(request: LoginRequest): DataOrException<LoginResponse, Boolean, Exception>

    suspend fun getAll(token: String): DataOrException<ApiResponse, Boolean, Exception>

    suspend fun addOne(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>
    suspend fun addMultiple(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>

    suspend fun updateOne(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>
    suspend fun updateMultiple(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>

    suspend fun deleteOne(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>
    suspend fun deleteMultiple(token: String , request: ApiRequest):  DataOrException<ApiResponse, Boolean, Exception>
}