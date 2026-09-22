package com.vayalink.app.data.remote

import com.vayalink.app.data.model.Alert
import com.vayalink.app.data.model.IncidentReport
import com.vayalink.app.data.model.Route
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Defines the RESTful API contract consumed by the app, matching the
 * "Proposed endpoints" table in the Planning & Design document.
 *
 * Point this at your own hosted REST API (e.g. a free mockapi.io project,
 * or your own Node/Express + database backend) by editing
 * [RetrofitClient.BASE_URL].
 */
interface ApiService {

    @GET("routes")
    suspend fun getRoutes(): List<Route>

    @GET("routes/{id}")
    suspend fun getRoute(@Path("id") routeId: String): Route

    @GET("alerts")
    suspend fun getAlerts(): List<Alert>

    @POST("alerts")
    suspend fun submitReport(@Body report: IncidentReport): Response<Unit>
}
