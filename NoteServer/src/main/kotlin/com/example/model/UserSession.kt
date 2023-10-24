package com.example.model

import io.ktor.server.auth.*

data class UserSession(
    val sub: String,
    val name: String
) :Principal