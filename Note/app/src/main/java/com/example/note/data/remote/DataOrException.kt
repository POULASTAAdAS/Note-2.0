package com.example.note.data.remote

class DataOrException<T, B, E : Exception>(
    var data: T? = null,
    var loading: Boolean = false,
    var e: E? = null
)