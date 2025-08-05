package com.scorpio.distancecalculator.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_table")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val pace: Double,
//    duration in millis
    val duration: Long,
    val calories: Double,
    val distance: Float,
    val activityId: Long,
)
