package com.scorpio.distancecalculator.tracker

import android.location.Location
import com.scorpio.distancecalculator.db.LocationDao
import com.scorpio.distancecalculator.db.LocationEntity
import com.scorpio.distancecalculator.locationproducer.MLocationProducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RunningTracker(
    private val locationProducer: MLocationProducer,
    private val locationDao: LocationDao
) : ActivityTracker() {

    override val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var locationTrackingJob: Job? = null
    private var distanceCalculationJob: Job? = null

    private val distanceMutableFlow = MutableStateFlow(0f)
    val distanceFlow: StateFlow<Float> = distanceMutableFlow

    val speedStateFlow: StateFlow<Float> = combine(
        distanceMutableFlow,
        elapsedTimeFlow
    ) { distance, time ->
        val distanceInKms = distance / 1000f
        val timeInHrs = time / 3600f
        if (timeInHrs != 0f) distanceInKms / timeInHrs else 0f
    }.stateIn(
        scope,
        SharingStarted.WhileSubscribed(300),
        0f
    )

    private var lastTimestamp: Long = 0

    override suspend fun resume() {
        super.resume()
        println("DEBUG distanceCalculationJob")
        locationTrackingJob = scope.launch {
            locationProducer.startLocationUpdates().collectLatest { location ->
                locationDao.insertLocation(location.toEntity(currentActivityUUID))
                startDistanceCalculation()
            }
        }
    }

    private suspend fun startDistanceCalculation() {
        distanceCalculationJob?.join()
        distanceCalculationJob = scope.launch {
            val latestLocations =
                locationDao.getLastTenLocationsSync(lastTimestamp, currentActivityUUID)
            if (latestLocations.size >= 10) {
                lastTimestamp = latestLocations.maxOf { it.timestamp }
                val calculatedDistance = calculateDistance(latestLocations)
                distanceMutableFlow.value += calculatedDistance
            }
        }

    }

    override suspend fun pause() {
        locationTrackingJob?.cancel()
        distanceCalculationJob?.cancel()
        locationProducer.pauseLocationUpdates()
        super.pause()
    }

    override suspend fun finish() {
        locationTrackingJob?.cancel()
        distanceCalculationJob?.cancel()
        locationProducer.pauseLocationUpdates()
        distanceMutableFlow.value = 0f
        lastTimestamp = 0
        super.finish()
    }

    private fun calculateDistance(latestLocations: List<LocationEntity>): Float {
        if (latestLocations.size < 2) {
            distanceMutableFlow.value = 0f
            return 0f
        }

        var totalDistance = 0f
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
            totalDistance += distance[0]
        }
        return totalDistance
    }
}
