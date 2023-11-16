package com.example.note.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.utils.Constants.PREFERENCES_AUTH_TYPE_KEY
import com.example.note.utils.Constants.PREFERENCES_AUTO_SYNC_KEY
import com.example.note.utils.Constants.PREFERENCES_FIRST_TIME_SIGNED_IN_KEY
import com.example.note.utils.Constants.PREFERENCES_JWT_TOKEN_OR_SESSION_TOKEN_KEY
import com.example.note.utils.Constants.PREFERENCES_SIGNED_IN_KEY
import com.example.note.utils.Constants.PREFERENCES_SORT_STATE_KEY
import com.example.note.utils.Constants.PREFERENCES_USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreOperationImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreOperation {
    private object PreferencesKey {
        val signedInKey = booleanPreferencesKey(name = PREFERENCES_SIGNED_IN_KEY)
        val firstLogInState = booleanPreferencesKey(name = PREFERENCES_FIRST_TIME_SIGNED_IN_KEY)
        val authType = booleanPreferencesKey(name = PREFERENCES_AUTH_TYPE_KEY)
        val jwtTokenKey =
            stringPreferencesKey(name = PREFERENCES_JWT_TOKEN_OR_SESSION_TOKEN_KEY) // if true google auth else jwt
        val userNameKey = stringPreferencesKey(name = PREFERENCES_USERNAME)


        val autoSyncKey = booleanPreferencesKey(name = PREFERENCES_AUTO_SYNC_KEY)
        val sortStateKey = booleanPreferencesKey(name = PREFERENCES_SORT_STATE_KEY)
    }

    override suspend fun saveUpdateSignedInState(signedInState: Boolean) {
        dataStore.edit {
            it[PreferencesKey.signedInKey] = signedInState
        }
    }

    override fun readSignedInState(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val signedInState = it[PreferencesKey.signedInKey] ?: false
            signedInState
        }


    override suspend fun saveUpdateJWTTokenOrSession(jwtToken: String) {
        dataStore.edit {
            it[PreferencesKey.jwtTokenKey] = jwtToken
        }
    }

    override fun readJWTTokenOrSession(): Flow<String> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }.map {
            val jwtToken = it[PreferencesKey.jwtTokenKey] ?: ""

            jwtToken
        }

    override suspend fun saveFirstTimeLoginState(firstLogInState: Boolean) {
        dataStore.edit {
            it[PreferencesKey.firstLogInState] = firstLogInState
        }
    }

    override fun readFirstTimeLoginState(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val firstTimeSignedInState = it[PreferencesKey.firstLogInState] ?: false
            firstTimeSignedInState
        }

    override suspend fun saveAuthenticationType(authType: Boolean) {
        dataStore.edit {
            it[PreferencesKey.authType] = authType
        }
    }

    override fun readAuthType(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val authType = it[PreferencesKey.authType] ?: false
            authType
        }


    override suspend fun saveAutoSyncState(value: Boolean) {
        dataStore.edit {
            it[PreferencesKey.autoSyncKey] = value
        }
    }

    override fun readAutoSyncState(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val autoSync = it[PreferencesKey.autoSyncKey] ?: true
            autoSync
        }

    override suspend fun saveSortState(value: Boolean) {
        dataStore.edit {
            it[PreferencesKey.sortStateKey] = value
        }
    }

    override fun readSortState(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val sortState = it[PreferencesKey.sortStateKey] ?: true
            sortState
        }

    override suspend fun saveUserName(value: String) {
        dataStore.edit {
            it[PreferencesKey.userNameKey] = value
        }
    }

    override fun readUserName(): Flow<String> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }.map {
            it[PreferencesKey.userNameKey] ?: "User"
        }
}