package com.example.route.authentication

import com.example.model.EndPoint
import com.example.model.UserSession
import com.example.utils.Constants.AUDIENCE
import com.example.utils.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.googleAuthentication(tokenId: String) {

    val result = verifyTokenId(tokenId = tokenId)

    if (result != null) {
        // TODO Database

        val sub = result.payload["sub"].toString()
        val name = result.payload["name"].toString()

        call.sessions.set(UserSession(sub = sub, name = name))
        call.respondRedirect(EndPoint.Authorized.path)
    } else {
        application.log.info("TOKEN VERIFICATION FAILED")
        call.respondRedirect(EndPoint.UnAuthorized.path)
    }
}

private fun verifyTokenId(tokenId: String): GoogleIdToken? = try {
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(AUDIENCE))
        .setIssuer(ISSUER)
        .build()
        .verify(tokenId)
} catch (e: Exception) {
    null
}