package com.example.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Note(
    @BsonId
    val _id: String? = null,
    val heading: String? = null,
    val content: String? = null,
    val createDate: String? = null,
    val updateDate: String? = null,
    val edited: Int = 0,
    val pinned: Boolean = false,
    val syncState: Boolean = false
)