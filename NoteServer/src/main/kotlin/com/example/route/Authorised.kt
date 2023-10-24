package com.example.route

import com.example.model.EndPoint
import com.example.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorized() {
    authenticate( "google-auth","auth-jwt") {
        get(EndPoint.Authorized.path) {
            call.respond(
                message = LoginResponse(
                    message = "authorized"
                ),
                status = HttpStatusCode.OK
            )
        }
    }
}