package com.pomegranate.locationproducer

import kotlinx.coroutines.flow.Flow

interface MLocationProducer {
    fun startLocationUpdates(): Flow<MLocation>

    fun pauseLocationUpdates()
}