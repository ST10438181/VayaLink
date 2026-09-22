package com.vayalink.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Local Room database used to cache frequently-used routes so the app
 * remains usable with poor or no connectivity (NFR8, Offline Route Storage feature).
 */
@Database(entities = [SavedRouteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedRouteDao(): SavedRouteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vayalink_offline.db"
                ).build().also { INSTANCE = it }
            }
    }
}
