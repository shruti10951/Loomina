package com.shrujan.loomina.data.remote.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private val logging = HttpLoggingInterceptor().apply {
        // BASIC in release; BODY in debug
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder().create()

    // IMPORTANT
    // - For emulator hitting your laptop: http://10.0.2.2:8000
    // - For prod: http://api.loomina.com/
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.103:8000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)

}