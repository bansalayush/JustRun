package com.scorpio.distancecalculator.tracker

import kotlin.reflect.KProperty

class UUIDDelegate {
    private var currentValue: Long = System.currentTimeMillis()

    operator fun getValue(
        thisRef: Tracker,
        property: KProperty<*>,
    ): Long {
        if (thisRef.trackingState.value == TrackingState.TrackingStateFinished) {
            currentValue = System.currentTimeMillis()
        }
        return currentValue
    }
}
