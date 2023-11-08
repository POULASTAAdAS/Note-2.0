package com.example.note.data.repository

import android.util.Log
import com.example.note.data.remote.DataOrException
import com.example.note.data.remote.NoteApi
import com.example.note.domain.model.ApiRequest
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

    //---------------------------------

    override suspend fun getAll(token: String): DataOrException<ApiResponse, Boolean, Exception> {
        val result = try {
            Log.d("error getting all data: ", "called")
            noteApi.getAll(token = token)
        } catch (e: Exception) {
            Log.d("error getting all data: ", e.message.toString())
            return DataOrException(e = e)
        }

        return DataOrException(data = result)
    }

    //---------------------------------

    override suspend fun addOne(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.addOne(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding one: ", e.message.toString())
        DataOrException(e = e)
    }

    override suspend fun addMultiple(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.addMultiple(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding multiple: ", e.message.toString())
        DataOrException(e = e)
    }

    //---------------------------------

    override suspend fun updateOne(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.updateOne(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding multiple: ", e.message.toString())
        DataOrException(e = e)
    }

    override suspend fun updateMultiple(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.updateMultiple(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding multiple: ", e.message.toString())
        DataOrException(e = e)
    }

    //---------------------------------

    override suspend fun deleteOne(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.deleteOne(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding multiple: ", e.message.toString())
        DataOrException(e = e)
    }

    override suspend fun deleteMultiple(
        token: String,
        request: ApiRequest
    ): DataOrException<ApiResponse, Boolean, Exception> = try {
        val result = noteApi.deleteMultiple(token, request)
        DataOrException(data = result)
    } catch (e: Exception) {
        Log.d("error adding multiple: ", e.message.toString())
        DataOrException(e = e)
    }

    //---------------------------------
}