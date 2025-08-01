package com.scorpio.distancecalculator.locationproducer

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.scorpio.distancecalculator.MLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DefaultLocationProducer(
    private val actualProvider: FusedLocationProviderClient,
) : MLocationProducer {
    private var locationCallback: LocationCallback? = null

    private val locationRequest by lazy {
        LocationRequest().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates(): Flow<MLocation> =
        callbackFlow {
            locationCallback =
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        val locations = locationResult.locations
                        for (location in locations) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            val speedInMetersPerSecond = location.speed
                            Log.d(TAG, "Location received: $latitude, $longitude speed:${location.speed}")
                            trySend(MLocation(latitude, longitude))
                        }
                    }
                }
            locationCallback?.let {
                actualProvider.requestLocationUpdates(
                    locationRequest,
                    it,
                    Looper.getMainLooper(),
                )
            }

            awaitClose {
                removeLocationCallback()
            }
        }

    override fun pauseLocationUpdates() {
        removeLocationCallback()
    }

    private fun removeLocationCallback() {
        locationCallback?.let { callback ->
            actualProvider.removeLocationUpdates(callback)
            Log.d(TAG, "Location updates removed.")
        }
        locationCallback = null
    }

    companion object {
        private const val TAG = "DefaultLocationClient"
    }
}
