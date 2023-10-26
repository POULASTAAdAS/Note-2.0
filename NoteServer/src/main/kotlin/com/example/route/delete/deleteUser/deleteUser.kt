package com.example.route.delete.deleteUser

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
import io.ktor.server.sessions.*

fun Route.deleteUser(dataBaseOperation: NoteDataBaseOperation) {
    authenticate("google-auth", "auth-jwt") {
        delete(EndPoint.DeleteUser.path) {
            val claim =
                call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("username_password")?.asString()
            val sub = call.authentication.principal<UserSession>()?.sub

            if (claim != null) {
                val email = claim.split("_")[0]

                try {
                    dataBaseOperation.deleteJWTUser(email = email)

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            message = "user deleted jwt"
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
                    dataBaseOperation.deleteGoogleUser(sub)
                    call.sessions.clear<UserSession>()

                    call.respond(
                        message = ApiResponse(
                            status = true,
                            message = "user deleted google"
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
                call.respondRedirect(EndPoint.UnAuthorized.path)
            }
        }
    }
}