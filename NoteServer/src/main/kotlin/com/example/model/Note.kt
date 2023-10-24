package com.example.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Note(

    //TODO may change in future

    @BsonId
    val _id: String? = null,
    val heading: String? = null,
    val content: String? = null,
    val date: String? = null,
    val updateDate: String? = null,
    val opened: Int? = null,
    val syncState: Boolean = false
)