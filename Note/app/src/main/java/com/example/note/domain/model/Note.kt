package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
//    @BsonId // TODO
    val _id: String? = null,
    val heading: String? = null,
    val content: String? = null,
    val date: String? = null,
    val updateDate: String? = null,
    val opened: Int? = null,
    val syncState: Boolean = false
)