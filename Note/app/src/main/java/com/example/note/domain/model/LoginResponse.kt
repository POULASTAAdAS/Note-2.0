package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String? = null,
    var googleLogIn: Boolean? = null,
    val message : String? = null
)