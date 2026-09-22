package com.vayalink.app.data.repository

import com.vayalink.app.data.local.SavedRouteDao
import com.vayalink.app.data.local.SavedRouteEntity
import com.vayalink.app.data.model.Route
import com.vayalink.app.data.remote.ApiService
import com.vayalink.app.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Source of truth for route data: reads live routes from the hosted REST API
 * (GET /routes, GET /journeys) and manages the Room-backed offline cache
 * of a user's saved routes (FR8, FR15, FR20-22).
 */
class RouteRepository(
    private val apiService: ApiService,
    private val savedRouteDao: SavedRouteDao
) {

    /** GET /routes - only returns routes currently operating (FR11, FR12). */
    suspend fun getActiveRoutes(): Resource<List<Route>> {
        return try {
            val routes = apiService.getRoutes()
            Resource.Success(routes.filter { it.isActive })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load routes")
        }
    }

    /** Simple origin/destination search across the routes returned by the API (FR12). */
    suspend fun searchRoutes(origin: String, destination: String): Resource<List<Route>> {
        return when (val result = getActiveRoutes()) {
            is Resource.Success -> {
                val matches = result.data.filter {
                    it.origin.contains(origin, ignoreCase = true) &&
                        it.destination.contains(destination, ignoreCase = true)
                }
                Resource.Success(matches)
            }
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
        }
    }

    suspend fun getRouteById(routeId: String): Resource<Route> {
        return try {
            Resource.Success(apiService.getRoute(routeId))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load route details")
        }
    }

    fun observeSavedRoutes(userId: String): Flow<List<SavedRouteEntity>> =
        savedRouteDao.getSavedRoutes(userId)

    suspend fun saveRouteOffline(userId: String, route: Route) {
        savedRouteDao.saveRoute(
            SavedRouteEntity(
                userId = userId,
                routeId = route.routeId,
                routeName = route.routeName,
                origin = route.origin,
                destination = route.destination,
                baseFare = route.baseFare,
                estimatedTravelTime = route.estimatedTravelTime
            )
        )
    }

    suspend fun removeSavedRoute(route: SavedRouteEntity) = savedRouteDao.deleteRoute(route)

    suspend fun isRouteSaved(userId: String, routeId: String): Boolean =
        savedRouteDao.isRouteSaved(userId, routeId) > 0
}
