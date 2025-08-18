package com.shrujan.loomina.data.remote.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitProvider is a singleton object that configures and provides
 * Retrofit instances and API service implementations.
 *
 * - Configures OkHttp client with logging and timeouts
 * - Uses Gson for JSON serialization/deserialization
 * - Provides AuthApi instance for authentication requests
 */
object RetrofitProvider {

    // Logging interceptor to capture network request/response details
    // Level can be set to BASIC in release builds and BODY for debugging
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with logging and timeout settings
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)                 // Log network calls
        .connectTimeout(20, TimeUnit.SECONDS)    // Connection timeout
        .readTimeout(20, TimeUnit.SECONDS)       // Response read timeout
        .build()

    // Gson instance for JSON parsing
    private val gson = GsonBuilder().create()

    // Retrofit instance configured with base URL, Gson converter, and OkHttp client
    // ⚠️ Important:
    // - For Android emulator accessing local machine: http://10.0.2.2:8000
    // - For local LAN (your laptop IP): http://192.168.1.103:8000/
    // - For production: http://api.loomina.com/
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.103:8000/")   // Change this based on environment
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    // Exposed API service for authentication-related endpoints
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
}
