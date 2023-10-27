package com.example.note.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperation {
    suspend fun saveUpdateSignedInState(signedInState: Boolean)
    fun readSignedInState(): Flow<Boolean>

    suspend fun saveFirstTimeLoginState(firstLogInState: Boolean)
    fun readFirstTimeLoginState(): Flow<Boolean>

    suspend fun saveUpdateJWTToken(jwtToken: String)
    fun readJWTToken(): Flow<String>
}