package com.scorpio.distancecalculator

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.scorpio.distancecalculator.db.AppDatabase

class DistanceCalculatorApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    private val appScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        mContext = this
        // You can perform other application-wide initializations here if needed.
        // For example, if you wanted to ensure the database is created on app start,
        // you could access it here, but lazy initialization defers it.
        // val db = database // This would trigger the lazy initialization
    }

    companion object {
        lateinit var mContext: DistanceCalculatorApplication
    }
}