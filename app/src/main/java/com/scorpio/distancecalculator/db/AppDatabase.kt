package com.scorpio.distancecalculator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocationEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = false,
)
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
//                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration() // Use with caution for production apps
                        .build()
                Companion.instance = instance
                // return instance
                instance
            }
        }

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//
//            private fun calculateAndInsertActivityToDb(
//                db: SupportSQLiteDatabase,
//                activityId: Long
//            ) {
//                val locationsByIdCursor = db.query(
//                    "SELECT *  FROM locations_table where activityId = ? ORDER BY timestamp ASC",
//                    arrayOf(activityId)
//                )
//                val list = mutableListOf<LocationEntity>()
//                while (locationsByIdCursor.moveToNext()) {
//                    val id =
//                        locationsByIdCursor.getLong(locationsByIdCursor.getColumnIndexOrThrow("id"))
//                    val latitude =
//                        locationsByIdCursor.getDouble(locationsByIdCursor.getColumnIndexOrThrow("latitude"))
//                    val longitude =
//                        locationsByIdCursor.getDouble(locationsByIdCursor.getColumnIndexOrThrow("longitude"))
//                    val timestamp =
//                        locationsByIdCursor.getLong(locationsByIdCursor.getColumnIndexOrThrow("timestamp"))
//                    list.add(LocationEntity(id, latitude, longitude, timestamp, activityId))
//                }
//                val (distance, duration) = calculateFinalTimeAndDistance(list)
//            }
//
//            override fun migrate(db: SupportSQLiteDatabase) {
//                // 1. Create the new activity_table
//                db.execSQL(
//                    """
//            CREATE TABLE IF NOT EXISTS activity_table (
//                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
//                pace REAL NOT NULL,
//                duration INTEGER NOT NULL,
//                calories REAL NOT NULL,
//                distance REAL NOT NULL,
//                activityId INTEGER NOT NULL
//            )
//        """.trimIndent()
//                )
//
//                // 2. Process data from locations_table and insert into activity_table
//                val distinctActivityIdCursor =
//                    db.query("SELECT DISTINCT activityId FROM locations_table")
//                while (distinctActivityIdCursor.moveToNext()) {
//                    val activityId = distinctActivityIdCursor.getLong(
//                        distinctActivityIdCursor.getColumnIndexOrThrow("activityId")
//                    )
//                    calculateAndInsertActivityToDb(db, activityId)
//
//                }
//                val cursor = db.query("SELECT * FROM locations_table")
//                while (cursor.moveToNext()) {
//                    val activityId = cursor.getLong(cursor.getColumnIndexOrThrow("activityId"))
//                    // Example: Aggregate or process data as needed
//                    // For demonstration, insert dummy values
//                    db.execSQL(
//                        """
//                INSERT INTO activity_table (pace, duration, calories, distance, activityId)
//                VALUES (?, ?, ?, ?, ?)
//            """, arrayOf(0.0, 0L, 0.0, 0.0, activityId)
//                    )
//                }
//                cursor.close()
//            }
//        }
    }
}
