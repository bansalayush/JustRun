package com.scorpio.distancecalculator.locationproducer

import com.scorpio.distancecalculator.MLocation
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DummyLocationProducer : MLocationProducer {
    var dummyJob: Job? = null

    override fun startLocationUpdates(): Flow<MLocation> =
        callbackFlow {
            dummyJob =
                launch {
//            val dummyLocations = LOC.values()
//            while (true) {
//                dummyLocations.forEach { loc ->
//                    send(MLocation(loc.latitude, loc.longitude))
//                    delay(1000)
//                }
//            }
                }

            awaitClose {
                dummyJob?.cancel()
            }
        }

    override fun pauseLocationUpdates() {
        dummyJob?.cancel()
    }
}
