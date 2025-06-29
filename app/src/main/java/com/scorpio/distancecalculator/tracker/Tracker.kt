package com.scorpio.distancecalculator.tracker

import kotlinx.coroutines.flow.StateFlow

interface Tracker {
    val trackingState: StateFlow<TrackingState>
    val elapsedTimeFlow: StateFlow<Long>

    suspend fun resume()
    suspend fun pause()
    suspend fun finish()
}

enum class TrackingState {
    finished, active, paused
}
