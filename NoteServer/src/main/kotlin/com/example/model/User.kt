package com.example.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@Serializable
data class User(
    @BsonId
    val _id: String = ObjectId.get().toString(),
    // jwt authenticate
    val email: String? = null,
    val password: String? = null,
    // google authenticate
    val name: String? = null,
    val sub: String? = null,
    // userName
    val userName: String? = null,
    // data
    val listOfNote: List<Note>? = emptyList()
)