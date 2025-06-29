package com.scorpio.distancecalculator.locationproducer

import com.scorpio.distancecalculator.MLocation
import kotlinx.coroutines.flow.Flow

interface MLocationProducer {
    fun startLocationUpdates(): Flow<MLocation>
    fun pauseLocationUpdates()
}