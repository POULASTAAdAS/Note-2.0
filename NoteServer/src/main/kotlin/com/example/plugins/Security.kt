package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.model.EndPoint
import com.example.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import java.util.concurrent.TimeUnit

fun Application.configureSecurity() {
    val myRealm = environment.config.property("jwt.realm").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        session<UserSession>("google-auth") {
            validate {
                if (it.sub == this.sessions.get<UserSession>()?.sub)
                    it
                else
                    null
            }
            challenge {
                call.resolveResource(EndPoint.UnAuthorized.path)
            }
        }

        jwt("auth-jwt") {
            realm = myRealm

            verifier(jwkProvider, issuer) {
                acceptLeeway(3)
            }

            validate { credential ->
                if (credential.payload.getClaim("username_password").asString() != "")
                    JWTPrincipal(credential.payload)
                else
                    null
            }

            challenge { _, _ ->
                call.respondRedirect(EndPoint.UnAuthorized.path)
            }
        }
    }
}