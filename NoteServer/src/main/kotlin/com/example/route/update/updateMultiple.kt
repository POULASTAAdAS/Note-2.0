package com.example.route.update

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

fun Route.updateMultiple(dataBaseOperation: NoteDataBaseOperation) {
    authenticate("google-auth", "auth-jwt") {
        patch(EndPoint.UpdateMultiple.path) {
            val result = call.receive<ApiRequest>().listOfNote

            if (result != null) {
                val claim =
                    call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("username_password")?.asString()
                val sub = call.authentication.principal<UserSession>()?.sub

                if (claim != null) {
                    val email = claim.split("_")[0]

                    try {
                        dataBaseOperation.updateMultipleForJWTUser(result, email)

                        call.respond(
                            message = ApiResponse(
                                status = true,
                                message = "all updated jwt"
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
                        dataBaseOperation.updateMultipleForGoogleUser(result, sub)

                        call.respond(
                            message = ApiResponse(
                                status = true,
                                message = "all updated google"
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