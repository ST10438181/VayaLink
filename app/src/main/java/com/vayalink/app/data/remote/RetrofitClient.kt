package com.vayalink.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Builds the Retrofit + OkHttp client used to talk to the hosted REST API.
 *
 * IMPORTANT (for the demo video / PoE): replace BASE_URL with the address of
 * your own hosted backend (a mockapi.io project works well for the prototype,
 * e.g. "https://<your-project-id>.mockapi.io/api/v1/").
 */
object RetrofitClient {

    private const val BASE_URL = "https://6ab1a5dd9751d2b03e6d613f.mockapi.io/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
