package com.example.route.delete

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

fun Route.deleteOne(
    dataBaseOperation: NoteDataBaseOperation
) {
    authenticate("google-auth", "auth-jwt") {
        delete(EndPoint.DeleteOne.path) {
            val result = call.receive<ApiRequest>()._id

            if (result != null) {
                try {
                    dataBaseOperation.deleteOne(result)

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            message = "one deleted"
                        ),
                        status = HttpStatusCode.OK
                    )
                }catch (e :Exception){
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