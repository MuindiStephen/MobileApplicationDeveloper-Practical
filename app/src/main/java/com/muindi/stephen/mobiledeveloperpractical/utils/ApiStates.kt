package com.muindi.stephen.mobiledeveloperpractical.utils

/**
 * Sealed class to represent API states
 */
sealed class ApiStates<T>(val data: T? = null, val error: Throwable? = null) {
    class Success<T>(data: T?) : ApiStates<T>(data)
    class Loading<T>(data: T? = null) : ApiStates<T>(data)
    class Error<T>(throwable: Throwable?, data: T? = null) : ApiStates<T>(data, throwable)
}

