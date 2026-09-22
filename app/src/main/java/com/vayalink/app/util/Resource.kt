package com.vayalink.app.util

/**
 * Generic wrapper for anything that comes back from a repository (network or db),
 * so ViewModels/Activities can render loading, success and error states.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
