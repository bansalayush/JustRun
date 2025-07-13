package com.scorpio.distancecalculator.tracker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlin.coroutines.cancellation.CancellationException

abstract class ActivityTracker : Tracker {
    abstract val scope: CoroutineScope

    private val _trackingState = MutableStateFlow(TrackingState.finished)
    override val trackingState: StateFlow<TrackingState> = _trackingState

    private val _elapsedTimeFlow = MutableStateFlow(0L)
    override val elapsedTimeFlow: StateFlow<Long> = _elapsedTimeFlow

    private var elapsedTimeJob: Job? = null
    var currentActivityUUID = -1L
        private set
//    by UUIDDelegate()

    override suspend fun resume() {
        if (_trackingState.value == TrackingState.active) return
        if (_trackingState.value == TrackingState.finished)
            currentActivityUUID = System.currentTimeMillis()
        _trackingState.value = TrackingState.active
        elapsedTimeJob = scope.launch {
            while (true) {
                delay(1000)
                _elapsedTimeFlow.update { it + 1 }
            }
        }
    }

    override suspend fun pause() {
        _trackingState.value = TrackingState.paused
        elapsedTimeJob?.cancel(CancellationException("PAUSED ELAPSED TIME"))
        elapsedTimeJob = null
    }

    override suspend fun finish() {
        _trackingState.value = TrackingState.finished
        elapsedTimeJob?.cancel(CancellationException("FINISHED ELAPSED TIME"))
        elapsedTimeJob = null
        _elapsedTimeFlow.value = 0L
    }

    companion object {
        const val IDLE_TRACKING_UUID = -1L
    }
}
