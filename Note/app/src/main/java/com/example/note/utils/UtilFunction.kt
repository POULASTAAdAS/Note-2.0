package com.example.note.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.example.note.domain.model.InternalNote
import com.example.note.domain.model.Note
import com.example.note.domain.model.RecentlyDeletedNotes
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getCurrentDate(): LocalDate = LocalDate.now()

fun getTotalDeleteDate(): String = LocalDate.now().plusDays(30).toString()

fun getCurrentTime(): String = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))

fun getLocalDateFromString(value: String): LocalDate = LocalDate.parse(value)

fun getLeftDays(deleteDate: LocalDate): Int =
    ChronoUnit.DAYS.between(LocalDate.now(), deleteDate).toInt()


fun getAnnotatedSearchQuery(
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

fun getAnnotatedDeleteAccountText(
    color: Color
): AnnotatedString = buildAnnotatedString {
    append("Are you sure you want to ")
    withStyle(
        style = SpanStyle(
            color = color,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("delete")
    }
    append(" your ")
    withStyle(
        style = SpanStyle(
            color = color,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("account.")
    }
    append("\nAll ")
    withStyle(
        style = SpanStyle(
            color = color,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("data")
    }
    append(" will be ")
    withStyle(
        style = SpanStyle(
            color = color,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("lost.")
    }
}


fun modifyUserName(
    text: String,
    color: Color,
    fontSize: TextUnit
): AnnotatedString = buildAnnotatedString {
    append("Hi ")
    val first = text.first()
    withStyle(
        style = SpanStyle(
            color = color,
            fontSize = fontSize
        )
    ) {
        append(first)
    }
    append(text.drop(1))
}


fun firstLetterCapOfUserName(name: String): String {
    var userName = name.first().uppercase()

    userName += name.drop(1)

    return userName
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
                updateTime = it.updateTime,
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
    updateTime = recentlyDeletedNotes.updateTime,
    edited = recentlyDeletedNotes.edited,
    pinned = recentlyDeletedNotes.pinned,
    syncState = recentlyDeletedNotes.syncState
)