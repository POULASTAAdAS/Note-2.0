package com.example.plugins

import com.example.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSession>(name = "USER_SESSION")
    }
}