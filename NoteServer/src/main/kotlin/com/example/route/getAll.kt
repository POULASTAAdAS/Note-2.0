package com.example.route

import com.example.data.repository.NoteDataBaseOperation
import com.example.model.ApiResponse
import com.example.model.EndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAll(
    dataBaseOperation: NoteDataBaseOperation
) {
    authenticate ("google-auth", "auth-jwt") {
        get(EndPoint.GetAll.path) {
            call.respond(
                message = ApiResponse(
                    status = true,
                    listOfNote = dataBaseOperation.getAll()
                ),
                status = HttpStatusCode.OK
            )
        }
    }
}