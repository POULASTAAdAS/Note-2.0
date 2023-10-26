package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.data.repository.NoteDataBaseOperation
import com.example.route.authentication.loginSignup
import com.example.route.authorized
import com.example.route.delete.deleteData.deleteMultiple
import com.example.route.delete.deleteData.deleteOne
import com.example.route.delete.deleteUser.deleteUser
import com.example.route.insert.insertMultiple
import com.example.route.insert.insertOne
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


    val dataBaseOperation: NoteDataBaseOperation by KoinJavaComponent.inject(NoteDataBaseOperation::class.java)

    routing {
        loginSignup(jwkProvider, privateKeyString, audience, issuer, dataBaseOperation)
        root()
        authorized()

        insertOne(dataBaseOperation)
        insertMultiple(dataBaseOperation)

        updateOne(dataBaseOperation)
        updateMultiple(dataBaseOperation)

        deleteOne(dataBaseOperation)
        deleteMultiple(dataBaseOperation)

        deleteUser(dataBaseOperation)

        unAuthorized()

        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}