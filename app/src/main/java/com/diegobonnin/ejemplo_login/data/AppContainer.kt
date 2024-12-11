package com.diegobonnin.ejemplo_login.data


import android.app.Application
import android.content.Context
import com.diegobonnin.ejemplo_login.network.ApiService
import com.diegobonnin.ejemplo_login.ui.screens.CarsViewModel
import com.diegobonnin.ejemplo_login.ui.screens.LoginViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.net.CookieManager

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor


import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.net.CookiePolicy
import java.net.HttpCookie
import android.net.Uri
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI


interface AppContainer {
    val authRepository: AuthRepository
    val carRepository: CarRepository
    val loginViewModel: LoginViewModel
    val carsViewModel: CarsViewModel
}

class DefaultAppContainer() : AppContainer {

    private val baseUrl = "http://10.0.2.2:8080/"

    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private fun HttpUrl.toUri(): URI = URI(this.toString())

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                Log.d("CookieJar", "Saving cookies for URL: $url")
                Log.d("CookieJar", "Cookies to save: $cookies")

                cookies.forEach { cookie ->
                    cookieManager.getCookieStore().add(
                        url.toUri(),
                        HttpCookie(cookie.name, cookie.value).apply {
                            path = cookie.path
                            domain = cookie.domain
                            secure = cookie.secure
                        }
                    )
                }

                // Log stored cookies after saving
                val storedCookies = cookieManager.getCookieStore().get(url.toUri())
                Log.d("CookieJar", "Stored cookies after save: $storedCookies")
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                Log.d("CookieJar", "Loading cookies for URL: $url")

                val cookies = cookieManager.getCookieStore().get(url.toUri())
                    ?.mapNotNull { httpCookie ->
                        Cookie.Builder()
                            .name(httpCookie.name)
                            .value(httpCookie.value)
                            .path(httpCookie.path)
                            .domain(httpCookie.domain ?: url.host)
                            .build()
                    } ?: emptyList()

                Log.d("CookieJar", "Loaded cookies: $cookies")
                return cookies
            }
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    override val authRepository: AuthRepository by lazy {
        AuthRepository(apiService)
    }

    override val carRepository: CarRepository by lazy {
        CarRepository(apiService)
    }

    override val loginViewModel: LoginViewModel by lazy {
        LoginViewModel(authRepository)
    }

    override val carsViewModel: CarsViewModel by lazy {
        CarsViewModel(carRepository)
    }
}