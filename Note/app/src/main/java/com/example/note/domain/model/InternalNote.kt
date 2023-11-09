package com.example.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.utils.Constants.TABLE_NAME_INTERNAL

@Entity(tableName = TABLE_NAME_INTERNAL)
data class InternalNote(
    @PrimaryKey
    val id: Int,
    val heading: String? = null,
    val content: String? = null,
    val createDate: String,
    val updateDate: String,
    val edited: Int,
    val pinned: Boolean,
    val syncState: Boolean,
    val insert: Boolean = false,
    val update: Boolean = false,
    val delete: Boolean = false
)