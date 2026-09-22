package com.vayalink.app.data.repository

import com.vayalink.app.data.model.Alert
import com.vayalink.app.data.remote.ApiService
import com.vayalink.app.util.Resource

/**
 * Retrieves live/crowdsourced alerts from the hosted REST API (GET /alerts).
 * Backs the Live Alerts screen and push-notification content (FR17, FR19).
 */
class AlertRepository(private val apiService: ApiService) {

    suspend fun getAlerts(): Resource<List<Alert>> {
        return try {
            val alerts = apiService.getAlerts()
            // Most recent first
            Resource.Success(alerts.sortedByDescending { it.timestamp })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load alerts")
        }
    }

    suspend fun getAlertsForRoute(routeId: String): Resource<List<Alert>> {
        return when (val result = getAlerts()) {
            is Resource.Success -> Resource.Success(result.data.filter { it.routeId == routeId })
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
        }
    }
}
