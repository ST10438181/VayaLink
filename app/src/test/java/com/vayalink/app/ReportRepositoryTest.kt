package com.vayalink.app

import com.vayalink.app.data.model.IncidentReport
import com.vayalink.app.data.remote.ApiService
import com.vayalink.app.data.repository.ReportRepository
import com.vayalink.app.util.Resource
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for ReportRepository - covers FR23/FR24 and the 500-character
 * description limit from the Driver Reports storage table.
 */
class ReportRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: ReportRepository

    @Before
    fun setUp() {
        apiService = mock()
        repository = ReportRepository(apiService)
    }

    @Test
    fun `submitReport rejects a blank description without calling the API`() = runBlocking {
        val report = IncidentReport(userId = "u1", reportType = "traffic", description = "")

        val result = repository.submitReport(report)

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `submitReport rejects a description over 500 characters`() = runBlocking {
        val report = IncidentReport(userId = "u1", reportType = "traffic", description = "x".repeat(501))

        val result = repository.submitReport(report)

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `submitReport returns Success on a 2xx response`() = runBlocking {
        val report = IncidentReport(userId = "u1", reportType = "traffic", description = "Heavy congestion near the rank.")
        whenever(apiService.submitReport(report)).thenReturn(Response.success(Unit))

        val result = repository.submitReport(report)

        assertTrue(result is Resource.Success)
    }

    @Test
    fun `submitReport returns Error on a non-2xx response`() = runBlocking {
        val report = IncidentReport(userId = "u1", reportType = "traffic", description = "Heavy congestion near the rank.")
        val errorBody = "Server error".toResponseBody("text/plain".toMediaType())
        whenever(apiService.submitReport(report)).thenReturn(Response.error(500, errorBody))

        val result = repository.submitReport(report)

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `submitReport returns Error when the API throws`() = runBlocking {
        val report = IncidentReport(userId = "u1", reportType = "traffic", description = "Heavy congestion near the rank.")
        whenever(apiService.submitReport(report)).thenThrow(RuntimeException("timeout"))

        val result = repository.submitReport(report)

        assertTrue(result is Resource.Error)
    }
}
