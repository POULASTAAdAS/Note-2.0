package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val _id: String? = null,
    val note: Note? = null,
    val listOfNote: List<Note>? = null
)