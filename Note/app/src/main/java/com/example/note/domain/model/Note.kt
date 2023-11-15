package com.example.note.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.utils.Constants.TABLE_NAME_NOTE
import kotlinx.serialization.Serializable


@Entity(tableName = TABLE_NAME_NOTE)
@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val _id: Int = 0, // retrofit is changing string apiResponse to int

    val heading: String? = null,
    val content: String? = null,
    val createDate: String? = null,
    val updateDate: String? = null,
//    val updateTime: String,
    val edited: Int = 0,
    val pinned: Boolean = false,
    val syncState: Boolean = false
)