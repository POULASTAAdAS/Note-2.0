package com.example.note.utils

import android.util.Patterns
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration

class UrlVisualTransformation(
    private val color: Color
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText = TransformedText(
        buildAnnotatedStringWithUrlHighlighting(text, color),
        OffsetMapping.Identity
    )

    private fun buildAnnotatedStringWithUrlHighlighting(
        text: AnnotatedString,
        color: Color
    ): AnnotatedString = buildAnnotatedString {
        append(text)
        text.split("\\s+".toRegex()).filter {
            Patterns.WEB_URL.matcher(it).matches()
        }.forEach {
            val start = text.indexOf(it)
            val end = start + it.length

            addStyle(
                style = SpanStyle(
                    color = color,
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )
        }
    }
}