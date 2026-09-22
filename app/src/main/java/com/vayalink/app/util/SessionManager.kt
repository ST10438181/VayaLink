package com.vayalink.app.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Lightweight local store for user preferences that don't need Firestore
 * round-trips every time (e.g. dark mode, data saver) - complements FR6-FR10.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("vayalink_prefs", Context.MODE_PRIVATE)

    var preferredLanguage: String
        get() = prefs.getString(KEY_LANGUAGE, "EN") ?: "EN"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    var dataSaverMode: Boolean
        get() = prefs.getBoolean(KEY_DATA_SAVER, false)
        set(value) = prefs.edit().putBoolean(KEY_DATA_SAVER, value).apply()

    companion object {
        private const val KEY_LANGUAGE = "preferred_language"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_DATA_SAVER = "data_saver_mode"
    }
}
