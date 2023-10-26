package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String? = null, // this will return only for jwt login
    val userExists: String? = null,
    val googleLogIn: Boolean? = null,
    val message: String? = null,
    val apiResponse: ApiResponse? = null
)