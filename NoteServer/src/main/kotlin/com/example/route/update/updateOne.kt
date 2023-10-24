package com.example.route.update

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

fun Route.updateOne(
    dataBaseOperation: NoteDataBaseOperation
) {
    authenticate("google-auth", "auth-jwt") {
        patch(EndPoint.UpdateOne.path) {
            val result = call.receive<ApiRequest>().note

            if (result != null) {
                try {
                    if (dataBaseOperation.upDateOne(note = result))
                        call.respond(
                            message = ApiResponse(
                                status = true,
                                message = "one updated"
                            ),
                            status = HttpStatusCode.OK
                        )
                    else call.respond(
                        message = ApiResponse(
                            status = false,
                            message = "unable to update one"
                        ),
                        status = HttpStatusCode.ServiceUnavailable
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