package com.example.route.insert

import com.example.data.repository.NoteDataBaseOperation
import com.example.model.ApiRequest
import com.example.model.ApiResponse
import com.example.model.EndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addMultiple(
    dataBaseOperation: NoteDataBaseOperation
) {
    authenticate("google-auth", "auth-jwt") {
        post (EndPoint.AddMultiple.path) {
            val result = call.receive<ApiRequest>().listOfNote

            if (result != null) {
                try {
                    dataBaseOperation.addMultiple(listOfNote = result)

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            message = "all inserted"
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    call.respond(
                        message = ApiResponse(
                            status = false,
                            message = e.message.toString()
                        ),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            } else {
                call.respond(
                    message = ApiResponse(
                        status = false,
                        message = "apiRequest empty"
                    ),
                    status = HttpStatusCode.NoContent
                )
            }
        }
    }
}