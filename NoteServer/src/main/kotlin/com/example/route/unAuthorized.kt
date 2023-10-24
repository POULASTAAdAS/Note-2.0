package com.example.route

import com.example.model.EndPoint
import com.example.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unAuthorized() {
    get(EndPoint.UnAuthorized.path) {
        call.respond(
            message = LoginResponse(
                message = "unauthorized"
            ),
            status = HttpStatusCode.Unauthorized
        )
    }
}