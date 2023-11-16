package com.example.note.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperation {
    suspend fun saveUpdateSignedInState(signedInState: Boolean)
    fun readSignedInState(): Flow<Boolean>

    suspend fun saveFirstTimeLoginState(firstLogInState: Boolean)
    fun readFirstTimeLoginState(): Flow<Boolean>

    suspend fun saveUpdateJWTTokenOrSession(jwtToken: String)
    fun readJWTTokenOrSession(): Flow<String>

    suspend fun saveAuthenticationType(authType: Boolean)
    fun readAuthType(): Flow<Boolean>

    suspend fun saveAutoSyncState(value: Boolean)
    fun readAutoSyncState(): Flow<Boolean>

    suspend fun saveSortState(value: Boolean)
    fun readSortState(): Flow<Boolean>

    suspend fun saveUserName(value: String)
    fun readUserName(): Flow<String>
}