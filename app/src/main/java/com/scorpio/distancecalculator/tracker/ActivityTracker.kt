package com.scorpio.distancecalculator.tracker

import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateActive
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStatePaused
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class ActivityTracker : Tracker {
    abstract val scope: CoroutineScope

    private val _trackingState = MutableStateFlow(TrackingStateFinished)
    override val trackingState: StateFlow<TrackingState> = _trackingState

    private val _elapsedTimeFlow = MutableStateFlow(0L)
    override val elapsedTimeFlow: StateFlow<Long> = _elapsedTimeFlow

    private var elapsedTimeJob: Job? = null
    var currentActivityUUID = -1L
        private set
//    by UUIDDelegate()

    override suspend fun resume() {
        if (_trackingState.value == TrackingStateActive) return
        if (_trackingState.value == TrackingStateFinished) {
            currentActivityUUID = System.currentTimeMillis()
        }
        _trackingState.value = TrackingStateActive
        elapsedTimeJob =
            scope.launch {
                while (true) {
                    delay(1000)
                    _elapsedTimeFlow.update { it + 1 }
                }
            }
    }

    override suspend fun pause() {
        _trackingState.value = TrackingStatePaused
        elapsedTimeJob?.cancel(CancellationException("PAUSED ELAPSED TIME"))
        elapsedTimeJob = null
    }

    override suspend fun finish() {
        _trackingState.value = TrackingStateFinished
        elapsedTimeJob?.cancel(CancellationException("FINISHED ELAPSED TIME"))
        elapsedTimeJob = null
        _elapsedTimeFlow.value = 0L
    }

    companion object {
        const val IDLE_TRACKING_UUID = -1L
    }
}
