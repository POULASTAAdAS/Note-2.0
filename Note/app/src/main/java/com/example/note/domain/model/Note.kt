package com.example.note.domain.model

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val _id: String? = null,
    val heading: String? = null,
    val content: String? = null,
    val date: String? = null,
    val updateDate: String? = null,
    val edited: Int? = null,
    val pinned: Boolean? = null,
    val syncState: Boolean = false
)