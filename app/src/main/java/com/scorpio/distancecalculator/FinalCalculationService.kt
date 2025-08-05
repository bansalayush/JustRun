package com.scorpio.distancecalculator

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.scorpio.distancecalculator.RunningService.Companion.ACTIVITY_ID
import com.scorpio.distancecalculator.db.ActivityEntity
import com.scorpio.distancecalculator.db.AppDatabase
import kotlinx.coroutines.launch

class FinalCalculationService : LifecycleService() {
    private val activityDao by lazy {
        AppDatabase.getDatabase(DistanceCalculatorApplication.mContext).activityDao()
    }
    private val locationDao by lazy {
        AppDatabase.getDatabase(DistanceCalculatorApplication.mContext).locationDao()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        val activityId = intent?.extras?.getLong(ACTIVITY_ID)
        if (activityId != null && activityId != 0L) {
            lifecycleScope.launch {
                val (distance, duration) =
                    calculateFinalTimeAndDistance(
                        locationDao.getLocationsByActivityId(
                            activityId,
                        ),
                    )
                println("calculating activity data")
                activityDao.insertActivity(
                    ActivityEntity(
                        pace = 0.0,
                        duration = duration,
                        distance = distance,
                        calories = 0.0,
                        activityId = activityId,
                    ),
                )
                println("activity logged")
                stopSelf()
                // make entry in DB
                // kill this service
            }
        } else {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
