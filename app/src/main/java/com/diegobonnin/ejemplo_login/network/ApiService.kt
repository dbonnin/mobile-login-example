package com.diegobonnin.ejemplo_login.network

import com.diegobonnin.ejemplo_login.data.Car
import com.diegobonnin.ejemplo_login.data.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("users/login/{username}")
    suspend fun login(
        @Path("username") username: String,
        @Header("Authorization") basicAuth: String
    ): Response<User>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<User>

    @GET("cars")
    suspend fun getCars(): Response<List<Car>>
}