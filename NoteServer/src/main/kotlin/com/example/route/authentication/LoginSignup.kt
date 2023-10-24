package com.example.route.authentication

import com.auth0.jwk.JwkProvider
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
    issuer: String
) {
    post(EndPoint.LoginSignUp.path) {
        val loginRequest = call.receive<LoginRequest>()

        if (loginRequest.googleToken != null) {
            googleAuthentication(loginRequest.googleToken)
        } else if (loginRequest.email != null && loginRequest.password != null) {
            basicAuthentication(jwkProvider, privateKeyString, audience, issuer, loginRequest)
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