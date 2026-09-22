package com.vayalink.app.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

data class IncidentReport(
    @SerializedName("userId") val userId: String,
    @SerializedName("routeId") val routeId: String? = null,
    @SerializedName("reportType") val reportType: String,
    @SerializedName("description") val description: String,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("isAnonymous") val isAnonymous: Boolean = true,
    @SerializedName("alertId") val alertId: String = "rep-${UUID.randomUUID().toString().take(8)}",
    @SerializedName("alertType") val alertType: String = reportType,
    @SerializedName("timestamp") val timestamp: String = currentIsoTimestamp(),
    @SerializedName("isVerified") val isVerified: Boolean = false
) {
    companion object {
        private fun currentIsoTimestamp(): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            return format.format(Date())
        }
    }
}