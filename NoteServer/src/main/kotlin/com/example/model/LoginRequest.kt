package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val email: String? = null,
    val password: String? = null,
    val googleToken: String? = null
)