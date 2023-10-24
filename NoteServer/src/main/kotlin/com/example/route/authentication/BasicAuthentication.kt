package com.example.route.authentication

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.EndPoint
import com.example.model.LoginRequest
import com.example.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

suspend fun PipelineContext<Unit, ApplicationCall>.basicAuthentication(
    jwkProvider: JwkProvider,
    privateKeyString: String,
    audience: String,
    issuer: String,
    loginRequest: LoginRequest
) {
    val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey

    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

    val token = try {
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username_password", loginRequest.email + "_" + loginRequest.password)
            .withExpiresAt(Date(System.currentTimeMillis() + 999999999990L))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    } catch (e: Exception) {
        println(e.message.toString())
        null
    }

    // TODO database
    if (token != null) call.respond(
        message = LoginResponse(
            token = token
        ),
        status = HttpStatusCode.OK
    )
    else call.respondRedirect(EndPoint.UnAuthorized.path)
}