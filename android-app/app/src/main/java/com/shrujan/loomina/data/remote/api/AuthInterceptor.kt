package com.shrujan.loomina.data.remote.api

import android.content.Context
import com.shrujan.loomina.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * AuthInterceptor automatically attaches JWT to all requests.
 */
class AuthInterceptor(context: Context) : Interceptor {

    private val userPreferences = UserPreferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        // Get token synchronously from DataStore
        val token = runBlocking { userPreferences.token.first() }

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
