package com.example.note.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    fun observe(): Flow<STATUS>

    enum class STATUS {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST
    }
}