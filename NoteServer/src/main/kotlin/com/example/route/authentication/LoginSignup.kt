package com.example.route.authentication

import com.auth0.jwk.JwkProvider
import com.example.data.repository.NoteDataBaseOperation
import com.example.model.EndPoint
import com.example.model.LoginRequest
import com.example.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginSignup(
    jwkProvider: JwkProvider,
    privateKeyString: String,
    audience: String,
    issuer: String,
    dataBaseOperation: NoteDataBaseOperation
) {
    post(EndPoint.LoginSignUp.path) {
        val loginRequest = call.receive<LoginRequest>()

        if (loginRequest.googleToken != null) {
            googleAuthentication(loginRequest.googleToken, loginRequest.initial, dataBaseOperation)
        } else if (loginRequest.email != null && loginRequest.password != null) {
            jwtAuthentication(jwkProvider, privateKeyString, audience, issuer, loginRequest, dataBaseOperation)
        } else {
            call.respond(
                message = LoginResponse(
                    message = "google token and username password empty"
                ),
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}