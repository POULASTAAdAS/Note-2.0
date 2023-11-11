package com.example.note.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.note.domain.model.InternalNote
import com.example.note.domain.model.Note
import com.example.note.domain.model.RecentlyDeletedNotes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String =
    SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())

fun getCurrentTime(): String = SimpleDateFormat(
    "hh:mm a",
    Locale.getDefault()
).format(Date())

fun getAnnotatedString(
    text: String,
    searchQuery: String,
    color: Color
): AnnotatedString = buildAnnotatedString {
    text.forEach {
        if (
            searchQuery.contains(
                it,
                ignoreCase = true
            )
        ) {
            withStyle(
                style = SpanStyle(
                    color = color
                )
            ) {
                append(it)
            }
        } else append(it)
    }
}

fun convertListOfInternalNoteToNote(listOfInternalNote: List<InternalNote>): List<Note> {
    val listOfNote = ArrayList<Note>()

    listOfInternalNote.forEach {
        listOfNote.add(
            Note(
                _id = it.id,
                heading = it.heading,
                content = it.content,
                createDate = it.createDate,
                updateDate = it.updateDate,
                edited = it.edited,
                pinned = it.pinned,
                syncState = it.syncState
            )
        )
    }

    return listOfNote
}

fun getListOfIdFromInternalNote(listOfInternalNote: List<InternalNote>): List<Int> {
    val listOfId = ArrayList<Int>()

    listOfInternalNote.forEach {
        listOfId.add(it.id)
    }

    return listOfId
}

fun convertRecentlyDeletedNoteToNote(recentlyDeletedNotes: RecentlyDeletedNotes) = Note(
    heading = recentlyDeletedNotes.heading,
    content = recentlyDeletedNotes.content,
    createDate = recentlyDeletedNotes.createDate,
    updateDate = recentlyDeletedNotes.updateDate,
    edited = recentlyDeletedNotes.edited,
    pinned = recentlyDeletedNotes.pinned,
    syncState = recentlyDeletedNotes.syncState
)
