package com.scorpio.distancecalculator.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pomegranate.locationproducer.MLocationProducer
import com.scorpio.distancecalculator.db.AppDatabase
import com.scorpio.distancecalculator.db.LocationDao
import com.scorpio.distancecalculator.locationproducer.DefaultLocationProducer
import com.scorpio.distancecalculator.tracker.RunningTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLocationProducer(fusedLocationProviderClient: FusedLocationProviderClient): MLocationProducer {
        return DefaultLocationProducer(fusedLocationProviderClient)
    }

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }

    @Provides
    @Singleton
    fun provideRunningTracker(
        locationProducer: MLocationProducer,
        locationDao: LocationDao,
    ): RunningTracker {
        return RunningTracker(locationProducer, locationDao)
    }
}
