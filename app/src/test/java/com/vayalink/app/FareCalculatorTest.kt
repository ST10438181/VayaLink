package com.vayalink.app

import com.vayalink.app.data.model.Route
import com.vayalink.app.util.FareCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Unit tests for FareCalculator - covers FR13 (estimated fares).
 */
class FareCalculatorTest {

    @Test
    fun `estimateFare returns baseFare when distance is zero`() {
        val fare = FareCalculator.estimateFare(baseFare = 12.0, farePerKm = 1.5, distanceKm = 0.0)
        assertEquals(12.0, fare, 0.001)
    }

    @Test
    fun `estimateFare adds distance cost correctly`() {
        // 12 + (1.5 * 10) = 27.0
        val fare = FareCalculator.estimateFare(baseFare = 12.0, farePerKm = 1.5, distanceKm = 10.0)
        assertEquals(27.0, fare, 0.001)
    }

    @Test
    fun `estimateFare rounds to nearest 50 cents`() {
        // 10 + (1.3 * 3) = 13.9 -> rounds to 14.0
        val fare = FareCalculator.estimateFare(baseFare = 10.0, farePerKm = 1.3, distanceKm = 3.0)
        assertEquals(14.0, fare, 0.001)
    }

    @Test
    fun `estimateFare throws for negative baseFare`() {
        assertThrows(IllegalArgumentException::class.java) {
            FareCalculator.estimateFare(baseFare = -5.0, farePerKm = 1.0, distanceKm = 5.0)
        }
    }

    @Test
    fun `estimateFare works from a Route object`() {
        val route = Route(
            routeId = "r1",
            routeName = "Khayelitsha - Bellville",
            origin = "Khayelitsha Site B",
            destination = "Bellville Station",
            baseFare = 15.0,
            farePerKm = 2.0,
            estimatedTravelTime = 45,
            distanceKm = 12.0,
            isActive = true
        )
        // 15 + (2 * 12) = 39.0
        assertEquals(39.0, FareCalculator.estimateFare(route), 0.001)
    }
}
