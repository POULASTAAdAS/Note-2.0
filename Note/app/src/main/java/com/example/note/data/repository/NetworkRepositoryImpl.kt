package com.example.note.data.repository

import android.util.Log
import com.example.note.data.remote.DataOrException
import com.example.note.data.remote.NoteApi
import com.example.note.domain.model.ApiResponse
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse
import com.example.note.domain.repository.NetworkRepository
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val noteApi: NoteApi
) : NetworkRepository {
    override suspend fun loginSignUp(request: LoginRequest): DataOrException<LoginResponse, Boolean, Exception> {
        val result = try {
            noteApi.loginSignup(request = request)
        } catch (e: Exception) {
            Log.d("error Logging in: ", e.message.toString())
            return DataOrException(e = e)
        }

        return DataOrException(data = result)
    }

    override suspend fun getAll(token: String): DataOrException<ApiResponse, Boolean, Exception> {
        val result = try {
            Log.d("error getting all data: ", "called")

            noteApi.getAll(token = token)
//            noteApi.getAll()
        } catch (e: Exception) {
            Log.d("error getting all data: ", e.message.toString()) // todo error
            return DataOrException(e = e)
        }

        return DataOrException(data = result)
    }
}