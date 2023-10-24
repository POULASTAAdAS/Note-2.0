package com.example.note.domain.repository

import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse

interface NetworkRepository {
    suspend fun loginSignUp(request: LoginRequest): DataOrException<LoginResponse, Boolean, Exception>
}