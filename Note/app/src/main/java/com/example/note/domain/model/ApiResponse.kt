package com.example.note.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val status: Boolean,
    val message: String? = null,
    val note: Note? = null,
    val listOfNote: List<Note> = emptyList()
)