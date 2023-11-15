package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String? = null,
    val userExists: String? = null,
    val googleLogIn: Boolean? = null,
    val message: String? = null,
    val userName: String? = null
)