package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val listOfId: List<String>? = null,
    val note: Note? = null,
    val listOfNote: List<Note>? = null,
    val userName: String? = null
)