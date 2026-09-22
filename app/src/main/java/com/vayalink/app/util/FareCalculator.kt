package com.vayalink.app.util

import com.vayalink.app.data.model.Route
import kotlin.math.round

/**
 * Computes an estimated fare for a route: baseFare + (farePerKm * distanceKm).
 * Extracted as a standalone, side-effect-free function so it can be unit
 * tested directly (see FareCalculatorTest) - this backs FR13 (estimated fares).
 */
object FareCalculator {

    fun estimateFare(route: Route): Double {
        val raw = route.baseFare + (route.farePerKm * route.distanceKm)
        // round to the nearest 50c, since taxi fares are typically quoted this way
        return round(raw * 2) / 2.0
    }

    fun estimateFare(baseFare: Double, farePerKm: Double, distanceKm: Double): Double {
        require(baseFare >= 0) { "baseFare must not be negative" }
        require(farePerKm >= 0) { "farePerKm must not be negative" }
        require(distanceKm >= 0) { "distanceKm must not be negative" }
        val raw = baseFare + (farePerKm * distanceKm)
        return round(raw * 2) / 2.0
    }
}
