package com.scorpio.distancecalculator

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.pomegranate.tracker.TrackerCommands.TrackerCommandFinish
import com.pomegranate.tracker.TrackerCommands.TrackerCommandPause
import com.pomegranate.tracker.TrackerCommands.TrackerCommandResume
import com.scorpio.distancecalculator.notification.NOTIFICATION_CHANNEL_ID
import com.scorpio.distancecalculator.tracker.RunningTracker
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunningService : LifecycleService() {
    private lateinit var currentNotification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private var notificationUpdateJob1: Job? = null
    private var notificationUpdateJob2: Job? = null

    @Inject
    lateinit var runningTracker: Lazy<RunningTracker>

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        intent?.action?.let {
            when (it) {
                TrackerCommandResume.toString() -> {
                    lifecycleScope.launch {
//                        LoggerProvider.getLogger().logEvent(RUNNING_SERVICE_STARTED)
                        runningTracker.get().resume()
                    }
                    handleNotification()
                }

                TrackerCommandPause.toString() -> {
                    lifecycleScope.launch {
//                        LoggerProvider.getLogger().logEvent(RUNNING_SERVICE_PAUSED)
                        runningTracker.get().pause()
                    }
                    handleNotification()
                }

                TrackerCommandFinish.toString() -> {
                    lifecycleScope.launch {
                        runningTracker.get().finish()
                        notificationUpdateJob1?.cancel()
                        notificationUpdateJob2?.cancel()
                        notificationManager.cancel(1)
                        startFinalCalculationService()
                        // send a signal to mainactivity to calculate the final distance and elapsed time
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                }

                else -> {}
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun handleNotification() {
        if (PackageManager.PERMISSION_GRANTED ==
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        ) {
            currentNotification =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("JustRun")
                    .setAutoCancel(false)
                    .setContentIntent(getMainActivityIntent())
                    .setOngoing(true)
            startForeground(1, currentNotification.build())
            updateNotification()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun updateNotification() {
        notificationUpdateJob1 =
            lifecycleScope.launch {
                runningTracker.get().distanceFlow.collectLatest {
                    val distance = formatDistanceToKmSimple(it)
                    currentNotification.setContentTitle(
                        "Distance $distance(kms)",
                    )
                    notificationManager.notify(1, currentNotification.build())
                }
            }
        notificationUpdateJob2 =
            lifecycleScope.launch {
                runningTracker.get().elapsedTimeFlow.collectLatest {
                    val elapsedTime = formatDuration(it)
                    currentNotification.setContentText(
                        "Elapsed-Time $elapsedTime(mm:ss)",
                    )
                    notificationManager.notify(1, currentNotification.build())
                }
            }
    }

    private fun getMainActivityIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            LAUNCH_MAINACTIIVTY_PENDING_INTENT_REQUEST_CODE,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun startFinalCalculationService() {
        Intent(
            applicationContext,
            FinalCalculationService::class.java,
        ).apply {
            putExtra(ACTIVITY_ID, runningTracker.get().currentActivityUUID)
        }.also { this@RunningService.startService(it) }
    }

    companion object {
        const val LAUNCH_MAINACTIIVTY_PENDING_INTENT_REQUEST_CODE = 143
        const val ACTIVITY_ID = "activity_id"
    }
}
