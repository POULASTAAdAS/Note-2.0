package com.example.note.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.utils.Constants.TABLE_NAME
import kotlinx.serialization.Serializable


@Entity(tableName = TABLE_NAME)
@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val _id: Int = 0, // retrofit is changing string apiResponse to int

    val heading: String? = null,
    val content: String? = null, // TODO make content not nullable
    val createDate: String? = null, // TODO make createDate and updateDate not nullable and change type to date
    val updateDate: String? = null,
    val edited: Int = 0,
    val pinned: Boolean = false, // TODO make pinned not nullable
    val syncState: Boolean = false
)