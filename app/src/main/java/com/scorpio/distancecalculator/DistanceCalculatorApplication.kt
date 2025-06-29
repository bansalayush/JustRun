package com.scorpio.distancecalculator

import android.app.Application
import com.scorpio.distancecalculator.AppDatabase

class DistanceCalculatorApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        // You can perform other application-wide initializations here if needed.
        // For example, if you wanted to ensure the database is created on app start,
        // you could access it here, but lazy initialization defers it.
        // val db = database // This would trigger the lazy initialization
    }

    companion object {
        lateinit var mContext: DistanceCalculatorApplication
    }
}