package com.example.route.read

import com.example.data.repository.NoteDataBaseOperation
import com.example.model.ApiResponse
import com.example.model.EndPoint
import com.example.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAll(dataBaseOperation: NoteDataBaseOperation) {
    authenticate("google-auth", "auth-jwt") {
        get(EndPoint.GetAll.path) {
            val claim =
                call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("username_password")?.asString()
            val sub = call.authentication.principal<UserSession>()?.sub

            if (claim != null) {
                val email = claim.split("_")[0]

                try {
                    val listOfNote = dataBaseOperation.getAllNoteForJWTAuthenticatedUser(email = email)

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            listOfNote = listOfNote,
                            message = "jwt"
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
                    val listOfNote = dataBaseOperation.getAllNoteForGoogleAuthenticatedUser(sub)

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            listOfNote = listOfNote,
                            message = "google"
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
            }
            call.respond(
                message = ApiResponse(
                    status = false,
                    message = "claim,sub both are empty"
                ),
                status = HttpStatusCode.NoContent
            )
        }
    }
}