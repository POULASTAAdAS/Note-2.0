package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest( // TODO unfinished
    val _id: String? = null,
    val note: Note? = null,
    val listOfNote: List<Note>? = null
)