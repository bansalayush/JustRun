package com.scorpio.distancecalculator

import android.location.Location
import com.scorpio.distancecalculator.db.LocationEntity
import kotlin.time.Duration.Companion.seconds

inline fun formatDuration(totalSeconds: Long): String {
    if (totalSeconds < 0) return "00:00:00"

    val duration = totalSeconds.seconds
    val minutes = duration.inWholeMinutes % 60
    val seconds = duration.inWholeSeconds % 60

    return String.format("%02d:%02d", minutes, seconds)
}

inline fun formatDistanceToKmSimple(totalMeters: Float): String {
    if (totalMeters < 0) return "0.00"
    val kilometers = totalMeters / 1000.0
    return String.format("%.2f", kilometers) // Formats to 2 decimal places
}

fun calculateFinalTimeAndDistance(latestLocations: List<LocationEntity>): Pair<Float, Long> {
    if (latestLocations.size < 2) {
        return Pair(0f, 0L)
    }
    var totalDistance = 0f
    var totalTime = 0L
    for (i in 1 until latestLocations.size) {
        val start = latestLocations[i - 1]
        val end = latestLocations[i]
        val distance = FloatArray(1)
        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            distance
        )
        totalTime += (end.timestamp - start.timestamp)
        totalDistance += distance[0]
    }
    return Pair(totalDistance, totalTime)
}