package com.scorpio.distancecalculator.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scorpio.distancecalculator.dataStore
// import com.scorpio.logger.LoggerProvider

class DbCleanupWork(
    private val appContext: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    private val workerExecutedAtKey = stringPreferencesKey("worker_executed_at")

    override suspend fun doWork(): Result {
//        LoggerProvider.getLogger().logEvent(WORKER_STARTED)

        appContext.dataStore.edit { prefs ->
            // last executed time comes on top
            prefs[workerExecutedAtKey] =
                "${System.currentTimeMillis()}\n" + prefs[workerExecutedAtKey]
        }

        println("Running DB cleanup work")

//        LoggerProvider.getLogger().logEvent(WORKER_ENDED)
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "DbCleanupWork"
        const val CLEANUP_WORK_REPEAT_INTERVAL = 20L
    }
}
