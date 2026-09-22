package com.vayalink.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * A minibus taxi route, retrieved from the hosted RESTful API (GET /routes).
 * Mirrors the "Routes" entity from the Planning & Design document.
 */
data class Route(
    @SerializedName("routeId") val routeId: String,
    @SerializedName("routeName") val routeName: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("baseFare") val baseFare: Double,
    @SerializedName("farePerKm") val farePerKm: Double,
    @SerializedName("estimatedTravelTime") val estimatedTravelTime: Int, // minutes
    @SerializedName("distanceKm") val distanceKm: Double,
    @SerializedName("isActive") val isActive: Boolean = true
)
