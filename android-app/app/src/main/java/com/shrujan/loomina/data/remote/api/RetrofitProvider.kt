package com.shrujan.loomina.data.remote.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitProvider provides Retrofit APIs with auth + logging.
 * Must be initialized with app Context once.
 */
class RetrofitProvider private constructor(context: Context) {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(AuthInterceptor(context)) // use context-aware interceptor
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder().create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.102:8000/") // change for prod
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
    val threadApi: ThreadApi = retrofit.create(ThreadApi::class.java)
    val storyApi: StoryApi = retrofit.create(StoryApi::class.java)
    val sparkApi: SparkApi = retrofit.create(SparkApi::class.java)

    companion object {
        @Volatile
        private var INSTANCE: RetrofitProvider? = null

        fun getInstance(context: Context): RetrofitProvider {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RetrofitProvider(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
