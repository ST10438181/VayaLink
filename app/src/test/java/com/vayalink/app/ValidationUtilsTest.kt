package com.vayalink.app

import com.vayalink.app.util.ValidationUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for ValidationUtils - covers FR2 (password rules) and the
 * report/alert description length constraints from the data-storage tables.
 *
 * NOTE: ValidationUtils.isValidEmail() uses android.util.Patterns, which is
 * only backed by a real implementation on-device or via Robolectric. Those
 * tests are covered separately in the androidTest source set; the tests
 * here focus on the pure logic that has no Android framework dependency.
 */
class ValidationUtilsTest {

    @Test
    fun `password shorter than 8 characters is invalid`() {
        assertFalse(ValidationUtils.isValidPassword("abc123"))
    }

    @Test
    fun `password of exactly 8 characters is valid`() {
        assertTrue(ValidationUtils.isValidPassword("abcd1234"))
    }

    @Test
    fun `password longer than 8 characters is valid`() {
        assertTrue(ValidationUtils.isValidPassword("aVeryLongSecurePassword123"))
    }

    @Test
    fun `blank report description is invalid`() {
        assertFalse(ValidationUtils.isValidReportDescription(""))
        assertFalse(ValidationUtils.isValidReportDescription("   "))
    }

    @Test
    fun `report description over 500 characters is invalid`() {
        val longDescription = "a".repeat(501)
        assertFalse(ValidationUtils.isValidReportDescription(longDescription))
    }

    @Test
    fun `report description of exactly 500 characters is valid`() {
        val description = "a".repeat(500)
        assertTrue(ValidationUtils.isValidReportDescription(description))
    }

    @Test
    fun `alert description over 300 characters is invalid`() {
        val longDescription = "a".repeat(301)
        assertFalse(ValidationUtils.isValidAlertDescription(longDescription))
    }

    @Test
    fun `normal alert description is valid`() {
        assertTrue(ValidationUtils.isValidAlertDescription("Heavy congestion on the N2 inbound."))
    }
}
