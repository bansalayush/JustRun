package com.scorpio.distancecalculator

import com.scorpio.distancecalculator.db.LocationEntity

data class MLocation(
    val latitude: Double,
    val longitude: Double,
//    val speed:Double,
) {
    fun toEntity(currentActivityUUID: Long): LocationEntity {
        return LocationEntity(
            latitude = this.latitude,
            longitude = this.longitude,
//            pace = this.speed,
            activityId = currentActivityUUID,
        )
    }
}
