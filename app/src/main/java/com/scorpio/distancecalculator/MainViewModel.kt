package com.scorpio.distancecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomegranate.tracker.TrackingState
import com.scorpio.distancecalculator.db.ActivityEntity
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.db.ToDeleteId
import com.scorpio.distancecalculator.tracker.RunningTracker
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val appDatabase: AppDatabase,
        private val runningTracker: Lazy<RunningTracker>,
    ) : ViewModel() {
        // todo: access this via usecase
        private val activitiesDao by lazy {
            appDatabase.activityDao()
        }

        val activitiesStateFlow: StateFlow<List<ActivityEntity>> =
            activitiesDao.getAllActivities().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

        //    val speedStateFlow: Flow<Float> = runningTracker.get().speedStateFlow
        val distanceFlow: Flow<String> =
            runningTracker.get().distanceFlow.map {
                formatDistanceToKmSimple(it)
            }
        val elapsedTimeFlow: Flow<String> =
            runningTracker.get().elapsedTimeFlow.map {
                formatDuration(it)
            }
        val trackingState: Flow<TrackingState> = runningTracker.get().trackingState

        fun deleteActivity(toDeleteId: Long) {
            viewModelScope.launch {
                activitiesDao.deleteActivity(ToDeleteId(toDeleteId))
            }
        }

        // this logic can break, WHY ?
        // in meantime if someone calls runningTracker.get().resume then the value of currentActivityUUID will change
        // check this angle once
    }
