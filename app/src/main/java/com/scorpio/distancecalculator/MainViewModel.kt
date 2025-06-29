package com.scorpio.distancecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.locationproducer.DefaultLocationProducer
import com.scorpio.distancecalculator.tracker.RunningTracker
import com.scorpio.distancecalculator.tracker.TrackingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainViewModel : ViewModel() {

    private val locationProducer by lazy {
        DefaultLocationProducer(
            LocationServices.getFusedLocationProviderClient(
                DistanceCalculatorApplication.mContext
            )
        )
    }

    private val locationDao by lazy {
        AppDatabase.getDatabase(DistanceCalculatorApplication.mContext).locationDao()
    }

    private val runningTracker by lazy {
        RunningTracker(
            locationProducer, locationDao
        )
    }

    val speedStateFlow: Flow<Float> = runningTracker.speedStateFlow
    val distanceFlow: Flow<String> = runningTracker.distanceFlow.map {
        formatDistanceToKmSimple(it)
    }
    val elapsedTimeFlow: Flow<String> = runningTracker.elapsedTimeFlow.map {
        formatDuration(it)
    }
    val trackingState: Flow<TrackingState> = runningTracker.trackingState

    fun pause() {
        viewModelScope.launch {
            runningTracker.pause()
        }
    }

    fun resume() {
        viewModelScope.launch {
            runningTracker.resume()
        }
    }

    fun finish() {
        viewModelScope.launch {
            runningTracker.finish()
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