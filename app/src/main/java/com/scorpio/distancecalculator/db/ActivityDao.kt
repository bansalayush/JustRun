package com.scorpio.distancecalculator.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("select * from activity_table order by activityId desc")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    @Delete(entity = ActivityEntity::class)
    suspend fun deleteActivity(activityId: ToDeleteId)
}

data class ToDeleteId(val activityId: Long)
