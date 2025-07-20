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
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.db.DbCleanupWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration.ofHours
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DistanceCalculatorApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    private val appScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        mContext = this
        println(
            measureTime {
                database
            },
        )

        // todo: move this to a helper class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "running-channel",
                    "Running Notifications",
                    NotificationManager.IMPORTANCE_MIN,
                )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        appScope.launch(Dispatchers.IO) {
            val key = stringPreferencesKey("db_cleanup_work_id")
            val value =
                dataStore.data.map { prefs ->
                    prefs[key]
                }.first()
            Timber.tag("CleanupWork-SETUP").d(value)
            if (value.isNullOrEmpty()) {
                this@DistanceCalculatorApplication.dataStore.edit { prefs ->
                    val work =
                        PeriodicWorkRequestBuilder<DbCleanupWork>(20, TimeUnit.MINUTES).build()
                    prefs[key] = work.id.toString()
                    WorkManager.getInstance(this@DistanceCalculatorApplication)
                        .enqueueUniquePeriodicWork(
                            DbCleanupWork.WORK_NAME,
                            ExistingPeriodicWorkPolicy.KEEP,
                            work,
                        )
                }
            }
        }
    }

    companion object {
        lateinit var mContext: DistanceCalculatorApplication
    }
}
