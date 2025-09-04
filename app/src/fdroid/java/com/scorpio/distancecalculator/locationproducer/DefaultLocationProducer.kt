package com.scorpio.distancecalculator.locationproducer

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.mapzen.android.lost.api.LocationListener
import com.mapzen.android.lost.api.LocationRequest
import com.mapzen.android.lost.api.LocationServices
import com.mapzen.android.lost.api.LostApiClient
import com.scorpio.distancecalculator.MLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

typealias LocationProviderClient = LostApiClient

object LocationProviderClientProvider {
    fun provideClient(context: Context): LocationProviderClient = LostApiClient.Builder(context).build()
}

class DefaultLocationProducer(
    private val actualProvider: LocationProviderClient,
) : MLocationProducer {
    private var locationCallback: LocationListener? = null

    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates(): Flow<MLocation> =
        callbackFlow {
            locationCallback =
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val speedInMetersPerSecond = location.speed
                        Log.d(
                            TAG,
                            "Location received: $latitude, $longitude speed:${location.speed}",
                        )
                        trySend(MLocation(latitude, longitude, speedInMetersPerSecond))
                    }
                }
            locationCallback?.let {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                    actualProvider,
                    locationRequest,
                    it,
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
            LocationServices.FusedLocationApi.removeLocationUpdates(actualProvider, callback)
            Log.d(TAG, "Location updates removed.")
        }
        locationCallback = null
    }

    companion object {
        private const val TAG = "DefaultLocationClient"
    }
}
