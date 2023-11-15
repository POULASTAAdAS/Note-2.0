package com.example.route.authentication

import com.example.data.repository.NoteDataBaseOperation
import com.example.model.EndPoint
import com.example.model.LoginResponse
import com.example.model.User
import com.example.model.UserSession
import com.example.utils.Constants.AUDIENCE
import com.example.utils.Constants.ISSUER
import com.example.utils.UserExists
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.googleAuthentication(
    tokenId: String,
    initial: Boolean?,
    dataBaseOperation: NoteDataBaseOperation
) {

    val result = verifyTokenId(tokenId = tokenId)

    if (result != null) {
        val sub = result.payload["sub"].toString()
        val name = result.payload["name"].toString()

        try {
            call.sessions.set(UserSession(sub = sub, name = name))

            when (
                dataBaseOperation.createGoogleAuthenticatedUser(
                    user = User(
                        sub = sub,
                        name = name
                    )
                )
            ) {
                UserExists.YES_SAME_PASSWORD -> {
                    if (initial != null && initial) {
                        try {
                            call.respond(
                                message = LoginResponse(
                                    userExists = UserExists.YES_SAME_PASSWORD.name,
                                    googleLogIn = true,
                                    userName = name
                                ),
                                status = HttpStatusCode.OK
                            )
                        } catch (e: Exception) {
                            call.respondRedirect(EndPoint.UnAuthorized.path)
                        }
                    } else {
                        call.respondRedirect(EndPoint.Authorized.path)
                    }
                }

                UserExists.YES_DIFF_PASSWORD -> {
                    call.respond(
                        message = LoginResponse(
                            message = "this should not happen"
                        ),
                        status = HttpStatusCode.InternalServerError
                    )
                }

                UserExists.NO -> {
                    call.respond(
                        message = LoginResponse(
                            userExists = UserExists.NO.name,
                            googleLogIn = true,
                            userName = name
                        ),
                        status = HttpStatusCode.OK
                    )
                }
            }
        } catch (e: Exception) {
            call.respondRedirect(EndPoint.UnAuthorized.path)
        }


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