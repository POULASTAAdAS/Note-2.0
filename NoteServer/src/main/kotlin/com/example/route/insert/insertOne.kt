package com.example.route.insert

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

fun Route.insertOne(dataBaseOperation: NoteDataBaseOperation) {
    authenticate("google-auth", "auth-jwt") {
        post(EndPoint.AddOne.path) {
            val result = call.receive<ApiRequest>().note

            if (result?._id != null) {
                val claim =
                    call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("username_password")?.asString()
                val sub = call.authentication.principal<UserSession>()?.sub

                if (claim != null) {
                    val email = claim.split("_")[0]

                    try {
                        if (dataBaseOperation.addOneForJWTUser(note = result, email = email))
                            call.respond(
                                message = ApiResponse(
                                    status = true,
                                    message = "one inserted"
                                ),
                                status = HttpStatusCode.OK
                            )
                        else
                            call.respond(
                                message = ApiResponse(
                                    status = false,
                                    message = "unable to insert jwt auth"
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

                    // TODO
                } else if (sub != null) {
                    try {
                        if (dataBaseOperation.addOneForGoogleUser(note = result, sub = sub))
                            call.respond(
                                message = ApiResponse(
                                    status = true,
                                    message = "one inserted"
                                ),
                                status = HttpStatusCode.OK
                            )
                        else
                            call.respond(
                                message = ApiResponse(
                                    status = false,
                                    message = "unable to insert"
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
                            message = "claim , sub both are empty"
                        ),
                        status = HttpStatusCode.NoContent
                    )
                }
            } else {
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
}