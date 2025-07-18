package com.scorpio.distancecalculator

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.scorpio.distancecalculator.notification.NOTIFICATION_CHANNEL_ID
import com.scorpio.distancecalculator.tracker.TrackerCommands.*
import com.scorpio.distancecalculator.tracker.TrackerProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RunningService : LifecycleService() {

    private lateinit var currentNotification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private var notificationUpdateJob_1: Job? = null
    private var notificationUpdateJob_2: Job? = null

    private val app by lazy { application as DistanceCalculatorApplication }
    private val runningTracker by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        TrackerProvider.getTracker(this)
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            println("Running Service received action: $it")
            when (it) {
                resume.toString() -> {
                    lifecycleScope.launch {
                        runningTracker.resume()
                    }
                    handleNotification(it)
                }

                pause.toString() -> {
                    lifecycleScope.launch {
                        runningTracker.pause()
                    }
                    handleNotification(it)
                }

                finish.toString() -> {
                    lifecycleScope.launch {
                        val activityUUID = runningTracker.finish()

                        notificationUpdateJob_1?.cancel()
                        notificationUpdateJob_2?.cancel()
                        notificationManager.cancel(1)
                        //send a signal to mainactivity to calculate the final distance and elapsed time
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                }

                else -> {}
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun handleNotification(string: String) {
        currentNotification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("JustRun")
            .setAutoCancel(false)
            .setContentIntent(getMainActivityIntent())
            .setOngoing(true)
        startForeground(1, currentNotification.build())
        updateNotification()
    }

    private fun updateNotification() {
        notificationUpdateJob_1 = lifecycleScope.launch {
            runningTracker.distanceFlow.collectLatest {
                val distance = formatDistanceToKmSimple(it)
                currentNotification.setContentTitle(
                    "Distance $distance(kms)"
                )
                notificationManager.notify(1, currentNotification.build())
            }
        }
        notificationUpdateJob_2 = lifecycleScope.launch {
            runningTracker.elapsedTimeFlow.collectLatest {
                val elapsedTime = formatDuration(it)
                currentNotification.setContentText(
                    "Elapsed-Time $elapsedTime(mm:ss)"
                )
                notificationManager.notify(1, currentNotification.build())
            }
        }
    }

    private fun getMainActivityIntent(): PendingIntent {
        return PendingIntent.getActivity(this, 143, Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }, PendingIntent.FLAG_IMMUTABLE)
    }
}