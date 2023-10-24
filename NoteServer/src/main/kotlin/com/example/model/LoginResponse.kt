package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String? = null, // this will return only for basic login
    val googleLogIn: Boolean? = null,
    val message : String? = null
)