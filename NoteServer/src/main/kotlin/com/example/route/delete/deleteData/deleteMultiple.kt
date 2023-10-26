package com.example.route.delete.deleteData

import com.example.data.repository.NoteDataBaseOperation
import com.example.model.ApiRequest
import com.example.model.ApiResponse
import com.example.model.EndPoint
import com.example.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteMultiple(dataBaseOperation: NoteDataBaseOperation) {
    authenticate("google-auth", "auth-jwt") {
        delete(EndPoint.DeleteMultiple.path) {
            val result = call.receive<ApiRequest>().listOfId

            if (result != null) {
                val claim =
                    call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("username_password")?.asString()
                val sub = call.authentication.principal<UserSession>()?.sub

                if (claim != null) {
                    val email = claim.split("_")[0]

                    try {
                        dataBaseOperation.deleteMultipleForJWTUser(result, email)

                        call.respond(
                            message = ApiResponse(
                                status = true,
                                message = "multiple deleted jwt"
                            ),
                            status = HttpStatusCode.OK
                        )
                    } catch (e: Exception) {
                        call.respond(
                            message = ApiResponse(
                                status = false,
                                message = e.message.toString()
                            ),
                            status = HttpStatusCode.OK
                        )
                    }
                } else if (sub != null) {
                    try {
                        dataBaseOperation.deleteMultipleForGoogleUser(result, sub)

                        call.respond(
                            message = ApiResponse(
                                status = true,
                                message = "one deleted google"
                            ),
                            status = HttpStatusCode.OK
                        )
                    } catch (e: Exception) {
                        call.respond(
                            message = ApiResponse(
                                status = false,
                                message = e.message.toString()
                            ),
                            status = HttpStatusCode.OK
                        )
                    }
                } else {
                    call.respond(
                        message = ApiResponse(
                            status = false,
                            message = "claim,sub both are empty"
                        ),
                        status = HttpStatusCode.NoContent
                    )
                }
            }

            call.respond(
                message = ApiResponse(
                    status = false,
                    message = "req body empty"
                ),
                status = HttpStatusCode.NoContent
            )
        }
    }
}