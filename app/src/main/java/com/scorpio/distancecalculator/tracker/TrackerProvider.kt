package com.scorpio.distancecalculator.tracker

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.locationproducer.DefaultLocationProducer

// to make this DI compatible move the DefaultLocationProducer, AppDatabase as constructor injectables
@Suppress("NoNameShadowing")
object TrackerProvider {
    @Volatile
    private var instance: RunningTracker? = null

    fun getTracker(context: Context): RunningTracker =
        instance ?: synchronized(this) {
            instance ?: let {
                val locationProducer =
                    DefaultLocationProducer(
                        LocationServices.getFusedLocationProviderClient(context),
                    )
                val locationDao = AppDatabase.getDatabase(context).locationDao()
                RunningTracker(locationProducer, locationDao).also { instance = it }
            }
        }
}
