package com.vayalink.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * A crowdsourced or admin-published alert (traffic, strike, delay, safety, road closure).
 * Retrieved from GET /alerts (FR17, FR19).
 */
data class Alert(
    @SerializedName("alertId") val alertId: String,
    @SerializedName("routeId") val routeId: String? = null,
    @SerializedName("alertType") val alertType: String, // traffic, strike, delay, safety, road_closure
    @SerializedName("description") val description: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("isVerified") val isVerified: Boolean = false
)
