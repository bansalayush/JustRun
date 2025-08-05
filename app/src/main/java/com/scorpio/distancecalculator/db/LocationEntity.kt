package com.scorpio.distancecalculator.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_table")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val speed: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val activityId: Long,
)
