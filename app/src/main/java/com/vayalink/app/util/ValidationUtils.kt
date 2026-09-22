package com.vayalink.app.util

import android.util.Patterns

/**
 * Pure, framework-light validation functions - kept separate from the UI
 * layer specifically so they are easy to unit test (see ValidationUtilsTest).
 */
object ValidationUtils {

    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /** FR2 spec: minimum 8 characters before Firebase hashes it with its own KDF. */
    fun isValidPassword(password: String): Boolean =
        password.length >= 8

    /** Driver / safety report description: required, max 500 chars (Driver Reports table). */
    fun isValidReportDescription(description: String): Boolean =
        description.isNotBlank() && description.length <= 500

    /** Crowdsourced alert description: required, max 300 chars (Crowdsourced Alerts table). */
    fun isValidAlertDescription(description: String): Boolean =
        description.isNotBlank() && description.length <= 300
}
