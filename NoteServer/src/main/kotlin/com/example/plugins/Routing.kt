package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.data.repository.NoteDataBaseOperation
import com.example.route.authentication.loginSignup
import com.example.route.authorized
import com.example.route.delete.deleteOne
import com.example.route.getAll
import com.example.route.insert.addMultiple
import com.example.route.insert.addOne
import com.example.route.root
import com.example.route.unAuthorized
import com.example.route.update.updateMultiple
import com.example.route.update.updateOne
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import java.io.File
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {

    val issuer = environment.config.property("jwt.issuer").getString()

    val privateKeyString = environment.config.property("jwt.privateKey").getString()
    val audience = environment.config.property("jwt.audience").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()


    routing {
        loginSignup(jwkProvider, privateKeyString, audience, issuer)
        root()
        authorized()

        val dataBaseOperation: NoteDataBaseOperation by KoinJavaComponent.inject(NoteDataBaseOperation::class.java)

        getAll(dataBaseOperation)

        addOne(dataBaseOperation)
        addMultiple(dataBaseOperation)

        updateOne(dataBaseOperation)
        updateMultiple(dataBaseOperation)

        deleteOne(dataBaseOperation)

        unAuthorized()

        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}