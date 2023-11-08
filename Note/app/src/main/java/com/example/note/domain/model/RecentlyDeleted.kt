package com.example.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.utils.Constants.TABLE_NAME_RECENTLY_DELETED

@Entity(tableName = TABLE_NAME_RECENTLY_DELETED)
data class RecentlyDeleted(
    @PrimaryKey
    val id: Int,
    val heading: String? = null,
    val content: String? = null,
    val createDate: String,
    val updateDate: String,
    val edited: Int = 0,
    val pinned: Boolean,
    val syncState: Boolean,
    val deleteDate: String
)