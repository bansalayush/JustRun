package com.scorpio.distancecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scorpio.distancecalculator.db.ActivityEntity
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.tracker.ActivityTracker.Companion.IDLE_TRACKING_UUID
import com.scorpio.distancecalculator.tracker.TrackerProvider
import com.scorpio.distancecalculator.tracker.TrackingState
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //todo: access this via usecase
    private val activitiesDao by lazy {
        AppDatabase.getDatabase(DistanceCalculatorApplication.mContext).activityDao()
    }

    val activitiesStateFlow: StateFlow<List<ActivityEntity>> = activitiesDao.getAllActivities().stateIn(
        scope= viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val runningTracker by lazy {
        TrackerProvider.getTracker(DistanceCalculatorApplication.mContext)
    }

//    val speedStateFlow: Flow<Float> = runningTracker.speedStateFlow
    val distanceFlow: Flow<String> =
        runningTracker.distanceFlow.map {
            formatDistanceToKmSimple(it)
        }
    val elapsedTimeFlow: Flow<String> =
        runningTracker.elapsedTimeFlow.map {
            formatDuration(it)
        }
    val trackingState: Flow<TrackingState> = runningTracker.trackingState

    // this logic can break, WHY ?
    // in meantime if someone calls runningTracker.resume then the value of currentActivityUUID will change
    // check this angle once
}
