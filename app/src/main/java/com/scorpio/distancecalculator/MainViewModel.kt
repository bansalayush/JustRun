package com.scorpio.distancecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.tracker.ActivityTracker.Companion.IDLE_TRACKING_UUID
import com.scorpio.distancecalculator.tracker.TrackerProvider
import com.scorpio.distancecalculator.tracker.TrackingState
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainViewModel : ViewModel() {
    private val runningTracker by lazy {
        TrackerProvider.getTracker(DistanceCalculatorApplication.mContext)
    }

    private val locationDao by lazy {
        AppDatabase.getDatabase(DistanceCalculatorApplication.mContext).locationDao()
    }

    val speedStateFlow: Flow<Float> = runningTracker.speedStateFlow
    val distanceFlow: Flow<String> =
        runningTracker.distanceFlow.map {
            formatDistanceToKmSimple(it)
        }
    val elapsedTimeFlow: Flow<String> =
        runningTracker.elapsedTimeFlow.map {
            formatDuration(it)
        }
    val trackingState: Flow<TrackingState> = runningTracker.trackingState

    // this logic can break, WHY ?
    // in meantime if someone calls runningTracker.resume then the value of currentActivityUUID will change
    // check this angle once

    // another way could be we get a return value from finish function . We send that data from Service via EventBus to ViewModel
    init {
        viewModelScope.launch {
            trackingState.collectLatest {
                if (it == TrackingStateFinished && runningTracker.currentActivityUUID != IDLE_TRACKING_UUID) {
                    makeFinalCalculations(runningTracker.currentActivityUUID)
                }
            }
        }
    }

    // create separate class ActivityDataAggregator to aggregate and put final data in a separate table
    private fun makeFinalCalculations(activityID: Long) {
        viewModelScope.launch {
            calculateFinalTimeAndDistance(locationDao.getLocationsByActivityId(activityID))
        }
    }

    private fun formatDistanceToKmSimple(totalMeters: Float): String {
        if (totalMeters < 0) return "0.00"
        val kilometers = totalMeters / 1000.0
        return String.format("%.2f", kilometers) // Formats to 2 decimal places
    }

    private fun formatDuration(totalSeconds: Long): String {
        if (totalSeconds < 0) return "00:00:00" // Or handle error appropriately

        val duration = totalSeconds.seconds // Create a Duration object
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60

        return String.format("%02d:%02d", minutes, seconds)
    }
}
