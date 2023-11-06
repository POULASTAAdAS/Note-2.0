package com.example.note.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDtTime(): String =
    SimpleDateFormat(
        "yy-MM-dd, hh:mm a",
        Locale.getDefault()
    ).format(Date())