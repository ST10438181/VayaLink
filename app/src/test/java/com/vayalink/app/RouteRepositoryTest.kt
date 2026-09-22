package com.vayalink.app

import com.vayalink.app.data.local.SavedRouteDao
import com.vayalink.app.data.local.SavedRouteEntity
import com.vayalink.app.data.model.Route
import com.vayalink.app.data.remote.ApiService
import com.vayalink.app.data.repository.RouteRepository
import com.vayalink.app.util.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for RouteRepository, with ApiService and SavedRouteDao mocked
 * out so no real network call or database is required (FR11, FR12, FR20).
 */
class RouteRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var savedRouteDao: SavedRouteDao
    private lateinit var repository: RouteRepository

    private val sampleRoutes = listOf(
        Route("r1", "Khayelitsha - CBD", "Khayelitsha", "Cape Town CBD", 15.0, 2.0, 40, 18.0, isActive = true),
        Route("r2", "Wynberg - Mitchells Plain", "Wynberg", "Mitchells Plain", 12.0, 1.5, 35, 14.0, isActive = true),
        Route("r3", "Old Route (retired)", "Bellville", "Parow", 8.0, 1.0, 15, 4.0, isActive = false)
    )

    @Before
    fun setUp() {
        apiService = mock()
        savedRouteDao = mock()
        repository = RouteRepository(apiService, savedRouteDao)
    }

    @Test
    fun `getActiveRoutes filters out inactive routes`() = runBlocking {
        whenever(apiService.getRoutes()).thenReturn(sampleRoutes)

        val result = repository.getActiveRoutes()

        assertTrue(result is Resource.Success)
        val routes = (result as Resource.Success).data
        assertEquals(2, routes.size)
        assertTrue(routes.all { it.isActive })
    }

    @Test
    fun `getActiveRoutes returns Error when the API call fails`() = runBlocking {
        whenever(apiService.getRoutes()).thenThrow(RuntimeException("Network unreachable"))

        val result = repository.getActiveRoutes()

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `searchRoutes matches on origin and destination case-insensitively`() = runBlocking {
        whenever(apiService.getRoutes()).thenReturn(sampleRoutes)

        val result = repository.searchRoutes("khayelitsha", "cbd")

        assertTrue(result is Resource.Success)
        val matches = (result as Resource.Success).data
        assertEquals(1, matches.size)
        assertEquals("r1", matches.first().routeId)
    }

    @Test
    fun `searchRoutes returns empty list when nothing matches`() = runBlocking {
        whenever(apiService.getRoutes()).thenReturn(sampleRoutes)

        val result = repository.searchRoutes("Nowhere", "Nowhere Else")

        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.isEmpty())
    }

    @Test
    fun `observeSavedRoutes delegates to the dao`() {
        val userId = "user123"
        val cached = listOf(
            SavedRouteEntity(userId = userId, routeId = "r1", routeName = "Test Route",
                origin = "A", destination = "B", baseFare = 10.0, estimatedTravelTime = 20)
        )
        whenever(savedRouteDao.getSavedRoutes(userId)).thenReturn(flowOf(cached))

        val flow = repository.observeSavedRoutes(userId)

        runBlocking {
            flow.collect { list ->
                assertEquals(1, list.size)
                assertEquals("r1", list.first().routeId)
            }
        }
    }
}
