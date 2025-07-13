package com.scorpio.distancecalculator.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations_table WHERE activityId=:currentActivityId ORDER BY timestamp ASC")
    suspend fun getLocationsByActivityId(currentActivityId: Long): List<LocationEntity>
    @Query("SELECT * FROM  locations_table WHERE timestamp>:lastTimestamp AND activityId=:currentActivityId ORDER BY timestamp ASC LIMIT 10 ")
    fun getLastTenLocationsSync(lastTimestamp: Long, currentActivityId: Long): List<LocationEntity>

    @Query("DELETE FROM locations_table")
    suspend fun deleteAllLocations()
}