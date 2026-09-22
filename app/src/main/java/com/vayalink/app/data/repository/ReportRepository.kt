package com.vayalink.app.data.repository

import com.vayalink.app.data.model.IncidentReport
import com.vayalink.app.data.remote.ApiService
import com.vayalink.app.util.Resource
import com.vayalink.app.util.ValidationUtils

/**
 * Submits driver-behaviour and safety/traffic reports to the hosted REST API
 * (POST /reports) - FR23, FR24.
 */
class ReportRepository(private val apiService: ApiService) {

    suspend fun submitReport(report: IncidentReport): Resource<Unit> {
        if (!ValidationUtils.isValidReportDescription(report.description)) {
            return Resource.Error("Description must be between 1 and 500 characters.")
        }
        return try {
            val response = apiService.submitReport(report)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Server rejected the report (code ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not submit report - check your connection")
        }
    }
}
