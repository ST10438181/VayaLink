package com.vayalink.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity caching a user's saved routes for offline viewing (FR20, FR22, NFR8).
 */
@Entity(tableName = "saved_routes")
data class SavedRouteEntity(
    @PrimaryKey val savedRouteId: String = UUID.randomUUID().toString(),
    val userId: String,
    val routeId: String,
    val routeName: String,
    val origin: String,
    val destination: String,
    val baseFare: Double,
    val estimatedTravelTime: Int,
    val savedAt: Long = System.currentTimeMillis()
)
