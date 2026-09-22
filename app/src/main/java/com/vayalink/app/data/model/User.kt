package com.vayalink.app.data.model

/**
 * Represents the authenticated user's profile.
 * The password itself is never stored here - Firebase Authentication
 * handles credential storage and hashing (see FR1, FR2).
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val preferredLanguage: String = "EN", // "EN" or "ZU" - FR9
    val notificationsEnabled: Boolean = true
)
