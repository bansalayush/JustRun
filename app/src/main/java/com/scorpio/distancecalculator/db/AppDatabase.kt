package com.scorpio.distancecalculator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun activityDao(): ActivityDao

    companion object {
        // Volatile to ensure atomic access to the variable
        @Volatile
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "locations" // Database file name

        fun getDatabase(context: Context): AppDatabase {
            // If the instance is not null, then return it,
            // if it is, then create the database
            return instance ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME,
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not covered in this basic example.
                        .fallbackToDestructiveMigration() // Use with caution for production apps
                        .build()
                Companion.instance = instance
                // return instance
                instance
            }
        }
    }
}
