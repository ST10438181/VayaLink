package com.vayalink.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedRouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoute(route: SavedRouteEntity)

    @Query("SELECT * FROM saved_routes WHERE userId = :userId ORDER BY savedAt DESC")
    fun getSavedRoutes(userId: String): Flow<List<SavedRouteEntity>>

    @Query("SELECT * FROM saved_routes WHERE userId = :userId ORDER BY savedAt DESC")
    suspend fun getSavedRoutesOnce(userId: String): List<SavedRouteEntity>

    @Delete
    suspend fun deleteRoute(route: SavedRouteEntity)

    @Query("SELECT COUNT(*) FROM saved_routes WHERE userId = :userId AND routeId = :routeId")
    suspend fun isRouteSaved(userId: String, routeId: String): Int
}
