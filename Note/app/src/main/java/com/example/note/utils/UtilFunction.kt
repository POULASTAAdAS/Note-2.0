package com.example.note.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
