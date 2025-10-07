package com.shrujan.loomina.utils

/**
 * ApiResult is a sealed class used to represent the result of API calls.
 * It helps in handling success and error cases in a type-safe way.
 *
 * Usage:
 * - ApiResult.Success -> contains the successful response data
 * - ApiResult.Error   -> contains an error message describing the failure
 */
sealed class ApiResult<out T> {

    /**
     * Represents a successful API response.
     *
     * @param data the actual data returned from the API
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Represents an error state from an API response or network call.
     *
     * @param message description of the error
     */
    data class Error(val message: String) : ApiResult<Nothing>()
}
