package com.example.route

import com.example.model.EndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.root() {
    get(EndPoint.Root.path) {
        call.respond(
            message = "root",
            status = HttpStatusCode.OK
        )
    }
}