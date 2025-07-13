package com.scorpio.distancecalculator.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scorpio.distancecalculator.dataStore
import timber.log.Timber

class DbCleanupWork(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    private val workerExecutedAtKey = stringPreferencesKey("worker_executed_at")

    override suspend fun doWork(): Result {
        appContext.dataStore.edit { prefs ->
            //last executed time comes on top
            prefs[workerExecutedAtKey] =
                "${System.currentTimeMillis()}\n" + prefs[workerExecutedAtKey]
        }

        println("Running DB cleanup work")
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "DbCleanupWork"
    }
}