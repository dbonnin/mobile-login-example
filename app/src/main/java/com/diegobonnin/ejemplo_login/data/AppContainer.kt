package com.diegobonnin.ejemplo_login.data

import android.content.Context
import com.diegobonnin.ejemplo_login.network.AuthService
import com.diegobonnin.ejemplo_login.network.TokenManager
import com.diegobonnin.ejemplo_login.network.UsersApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.Credentials
import okhttp3.logging.HttpLoggingInterceptor

interface AppContainer {
    val loginRepository: LoginRepository
}

class DefaultAppContainer: AppContainer {

    private val BASE_URL = "http://10.0.2.2:8080"

    private val tokenManager = TokenManager()

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        println("OkHttp --> $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configure JSON serializer
    private val json = Json {
        ignoreUnknownKeys = true // This helps with API flexibility
        coerceInputValues = true
    }

    // Create OkHttpClient with token interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = tokenManager.token.value
            val request = if (token != null) {
                chain.request().newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    // Create auth service for token requests
    private val authService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder()
            .addInterceptor { chain ->
                val credentials = Credentials.basic("client", "secret")
                val request = chain.request().newBuilder()
                    .header("Authorization", credentials)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(AuthService::class.java)

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: UsersApiService by lazy {
        retrofit.create(UsersApiService::class.java)
    }

    override val loginRepository: LoginRepository by lazy {
        RemoteLoginRepository(authService, retrofitService, tokenManager)
    }


}