package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val listOfId: List<Int>? = null,
    val note: Note? = null,
    val listOfNote: List<Note>? = null,
    val userName: String? = null
)